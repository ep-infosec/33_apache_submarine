#!/usr/bin/env bash
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

FWDIR="$(cd "$(dirname "$0")"; pwd)"
cd "$FWDIR"

# shellcheck disable=SC2034
SUBMARINE_PROJECT_PATH="$FWDIR/../.."

# build image
echo "Start building the mini-submarine docker image..."
docker build --tag pysubmarine-ci .

docker run --rm --pid=host \
           --privileged \
           -v "$SUBMARINE_PROJECT_PATH":/workspace \
           -e "CI_BUILD_HOME=/" \
           -e "CI_BUILD_USER=$(id -u -n)" \
           -e "CI_BUILD_UID=$(id -u)" \
           -e "CI_BUILD_GROUP=$(id -g -n)" \
           -e "CI_BUILD_GID=$(id -g)" \
           -e "PATH=/usr/bin/anaconda/envs/submarine-dev/bin:${PATH}" \
           -e "PYTHONPATH=python:/usr/bin/anaconda/envs/submarine-dev/bin/python"\
           -it pysubmarine-ci \
           /bin/bash --login /usr/local/bootstrap.sh bash
