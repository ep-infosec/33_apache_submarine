/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

module.exports = {
    "docs": [
        {
            "Getting Started": [
                "gettingStarted/quickstart",
                "gettingStarted/notebook",
                "gettingStarted/python-sdk",
                "gettingStarted/helm",
            ],
            "User Docs": [
                {
                    "Submarine SDK": [
                        "userDocs/submarine-sdk/submarine-cli",
                        "userDocs/submarine-sdk/submarine-client",
                        "userDocs/submarine-sdk/experiment-client",
                        "userDocs/submarine-sdk/tracking",
                    ],
                },
                {
                    "Others": [
                        "userDocs/others/mlflow",
                        "userDocs/others/tensorboard",
                    ],
                },
            ],
            "Developer Docs": [
                "devDocs/README",
                "devDocs/Dependencies",
                "devDocs/BuildFromCode",
                "devDocs/Development",
                "devDocs/IntegrationTestK8s",
                "devDocs/IntegrationTestE2E",
                "devDocs/HowToRelease",
                "devDocs/HowToVerify",
            ],
            "Community": [
                "community/README",
                "community/Bylaws",
                "community/HowToCommit",
                "community/contributing",
                "community/HowToVoteCommitterOrPMC",
                "community/HowToBecomeCommitter",
                "community/Resources",
            ],
            "Design Docs": [
                "designDocs/architecture-and-requirements",
                "designDocs/implementation-notes",
                "designDocs/environments-implementation",
                "designDocs/experiment-implementation",
                "designDocs/notebook-implementation",
                "designDocs/storage-implementation",
                {
                    "Submarine Server": [
                        "designDocs/submarine-server/architecture",
                        "designDocs/submarine-server/experimentSpec",
                    ],
                },
                {
                    "WIP Design Docs": [
                        "designDocs/wip-designs/submarine-launcher",
                        "designDocs/wip-designs/security-implementation",
                    ],
                },
            ],
        },
    ],
    "api": [
        "api/environment",
        "api/experiment",
        "api/experiment-template",
        "api/notebook",
        "api/register-model",
        "api/model-version",
        "api/serve",
    ],
};
