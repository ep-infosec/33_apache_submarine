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

package org.apache.submarine.server.submitter.k8s;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.submarine.server.api.spec.ExperimentSpec;
import org.apache.submarine.server.api.spec.NotebookSpec;

public abstract class SpecBuilder {
  // The spec files in test/resources
  public static final String tfJobReqFile = "/tf_mnist_req.json";
  public static final String pytorchJobReqFile = "/pytorch_job_req.json";
  public static final String xgboostJobReqFile = "/xgboost_job_req.json";
  protected final String pytorchJobWithEnvReqFile = "/pytorch_job_req_env.json";
  protected final String pytorchJobWithInvalidEnvReqFile =
      "/pytorch_job_req_invalid_env.json";
  protected final String notebookReqFile = "/notebook_req.json";
  protected final String pytorchJobWithHTTPGitCodeLocalizerFile =
      "/pytorch_job_req_http_git_code_localizer.json";
  protected final String pytorchJobWithSSHGitCodeLocalizerFile =
      "/pytorch_job_req_ssh_git_code_localizer.json";
  protected final String tfTfboardJobReqFile = "/tf_tfboard_mnist_req.json";

  protected final String H2_JDBC_URL = "jdbc:h2:mem:submarine-test;MODE=MYSQL;DB_CLOSE_DELAY=-1";
  protected final String H2_JDBC_DRIVERCLASS = "org.h2.Driver";
  protected final String H2_JDBC_USERNAME = "root";
  protected final String H2_JDBC_PASSWORD = "";

  protected Object buildFromJsonFile(Object obj, String filePath) throws IOException,
      URISyntaxException {
    Gson gson = new GsonBuilder().create();
    try (Reader reader = Files.newBufferedReader(getCustomJobSpecFile(filePath).toPath(),
        StandardCharsets.UTF_8)) {
      if (obj.equals(NotebookSpec.class)) {
        return gson.fromJson(reader, NotebookSpec.class);
      } else {
        ExperimentSpec experimentSpec = gson.fromJson(reader, ExperimentSpec.class);
        experimentSpec.getMeta().setExperimentId(
            String.format("experiment-%s-0001", System.currentTimeMillis())
        );
        return experimentSpec;
      }
    }
  }

  private File getCustomJobSpecFile(String path) throws URISyntaxException {
    URL fileUrl = this.getClass().getResource(path);
    return new File(fileUrl.toURI());
  }
}
