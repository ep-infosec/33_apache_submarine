#!/usr/bin/env bash
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# The script helps publishing release to maven.
# You need to specify release version and branch|tag name.
#
# Here's some helpful documents for the release.
# http://www.apache.org/dev/publishing-maven-artifacts.html
set -euo pipefail
BASEDIR="$(dirname "$0")"
. "${BASEDIR}/common_release.sh"

if [[ $# -ne 2 ]]; then
  usage
fi

for var in GPG_PASSPHRASE ASF_USERID ASF_PASSWORD; do
  if [[ -z "${!var:-}" ]]; then
    echo "You need ${var} variable set"
    exit 1
  fi
done

export MAVEN_OPTS="-Xmx2g -XX:MaxPermSize=512m"
RED='\033[0;31m'
NC='\033[0m' # No Color

RELEASE_VERSION="$1"
GIT_TAG="$2"
if [[ $RELEASE_VERSION == *"SNAPSHOT"* ]]; then
  DO_SNAPSHOT="yes"
fi

NEXUS_STAGING="https://repository.apache.org/service/local/staging"
# Get this from apache infra admin
NEXUS_PROFILE="2525cde13fad2a"

function cleanup() {
  echo "Remove working directory and maven local repository"
  rm -rf ${WORKING_DIR}
}

function curl_error() {
  ret=${1}
  if [[ $ret -ne 0 ]]; then
    echo "curl response code is: ($ret)"
    echo "See https://curl.haxx.se/libcurl/c/libcurl-errors.html to know the detailed cause of error."
    echo -e "${RED}Failed to publish maven artifact to staging repository."
    echo -e "IMPORTANT: You will have to re-run publish_release.sh to complete maven artifact publish.${NC}"
    cleanup
    exit 1
  fi
}


#
# Publishing Apache Submarine artifact to Apache snapshot repository.
#
function publish_snapshot_to_maven() {
  cd "${WORKING_DIR}/submarine"
  echo "Deploying Apache Submarine $RELEASE_VERSION version to snapshot repository."

  if [[ ! $RELEASE_VERSION == *"SNAPSHOT"* ]]; then
    echo "ERROR: Snapshots must have a version containing 'SNAPSHOT'"
    echo "ERROR: You gave version '$RELEASE_VERSION'"
    exit 1
  fi

  tmp_repo="$(mktemp -d /tmp/submarine-repo-XXXXX)"
  mvn versions:set -DnewVersion=$RELEASE_VERSION
  tmp_settings="tmp-settings.xml"
  echo "<settings><servers><server>" > $tmp_settings
  echo "<id>apache.snapshots.https</id><username>$ASF_USERID</username>" >> $tmp_settings
  echo "<password>$ASF_PASSWORD</password>" >> $tmp_settings
  echo "</server></servers></settings>" >> $tmp_settings

  mvn --settings $tmp_settings -Dmaven.repo.local="${tmp_repo}" -DskipTests deploy

  rm $tmp_settings
  rm -rf $tmp_repo
}

function publish_to_maven() {
  cd "${WORKING_DIR}/submarine"
  # Force release version
  mvn versions:set -DnewVersion="${RELEASE_VERSION}"

  # Using Nexus API documented here:
  # https://support.sonatype.com/hc/en-us/articles/213465868-Uploading-to-a-Staging-Repository-via-REST-API
  echo "Creating Nexus staging repository"
  repo_request="<promoteRequest><data><description>Apache Submarine ${RELEASE_VERSION}</description></data></promoteRequest>"
  out="$(curl -X POST -d "${repo_request}" -u "${ASF_USERID}:${ASF_PASSWORD}" \
    -H 'Content-Type:application/xml' -v \
    "${NEXUS_STAGING}/profiles/${NEXUS_PROFILE}/start")"
  create_ret=$?
  curl_error $create_ret
  staged_repo_id="$(echo ${out} | sed -e 's/.*\(orgapachesubmarine-[0-9]\{4\}\).*/\1/')"
  if [[ -z "${staged_repo_id}" ]]; then
    echo "Fail to create staging repository"
    exit 1
  fi

  echo "Created Nexus staging repository: ${staged_repo_id}"

  rm -rf $HOME/.m2/repository/org/apache/submarine

  # build and install to local
  echo "mvn clean install -DskipTests"
  mvn clean install -DskipTests
  if [[ $? -ne 0 ]]; then
    echo "Mvn install failed."
    exit 1
  fi

  pushd "${HOME}/.m2/repository/org/apache/submarine"
  find . -type f | grep -v '\.jar$' | grep -v '\.pom$' |grep -v '\.war$' | xargs rm

  echo "Creating hash and signature files"
  for file in $(find . -type f); do
    echo "${GPG_PASSPHRASE}" | gpg --passphrase-fd 0 --output "${file}.asc" \
      --detach-sig --armor "${file}"
    if [ $(command -v md5) ]; then
      # Available on OS X; -q to keep only hash
      md5 -q $file > $file.md5
    else
      # Available on Linux; cut to keep only hash
      md5sum $file | cut -f1 -d' ' > $file.md5
    fi
    ${SHASUM} -a 1 "${file}" | cut -f1 -d' ' > "${file}.sha1"
  done

  nexus_upload="${NEXUS_STAGING}/deployByRepositoryId/${staged_repo_id}"
  echo "Uploading files to ${nexus_upload}"
  for file in $(find . -type f); do
    # strip leading ./
    file_short="$(echo "${file}" | sed -e 's/\.\///')"
    dest_url="${nexus_upload}/org/apache/submarine/$file_short"
    echo "  Uploading ${file_short}"
    curl -u "${ASF_USERID}:${ASF_PASSWORD}" --upload-file "${file_short}" "${dest_url}"
    upload_ret=$?
    curl_error $upload_ret
  done

  echo "Closing nexus staging repository"
  repo_request="<promoteRequest><data><stagedRepositoryId>${staged_repo_id}</stagedRepositoryId><description>Apache Submarine ${RELEASE_VERSION}</description></data></promoteRequest>"
  out="$(curl -X POST -d "${repo_request}" -u "${ASF_USERID}:${ASF_PASSWORD}" \
    -H 'Content-Type:application/xml' -v \
    "${NEXUS_STAGING}/profiles/${NEXUS_PROFILE}/finish")"
  close_ret=$?
  curl_error $close_ret
  echo "Closed Nexus staging repository: ${staged_repo_id}"
  popd
  echo "Complete publishing maven artifacts to apache staging repository"
  echo "Once release candidate pass the vote, do not forget to hit the release button in https://repository.apache.org"
}

git_clone
if [[ "${DO_SNAPSHOT:-}" == 'yes' ]]; then
  publish_snapshot_to_maven
else
  publish_to_maven
fi
cleanup
