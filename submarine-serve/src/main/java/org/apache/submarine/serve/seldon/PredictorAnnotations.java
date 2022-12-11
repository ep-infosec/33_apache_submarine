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

package org.apache.submarine.serve.seldon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * SeldonDeployment.spec.predictors[*].annotations
 */
public class PredictorAnnotations {

  /**
   * <a href="https://docs.seldon.io/projects/seldon-core/en/latest/graph/custom_svc_name.html">
   *   Model with Custom Service Name Annotations
   * </a>
   */
  @SerializedName("seldon.io/svc-name")
  @JsonProperty("seldon.io/svc-name")
  private String serviceName;

  /**
   * Get predictor annotations with custom service name
   */
  public static PredictorAnnotations service(String serviceName) {
    return new PredictorAnnotations()
            .setServiceName(serviceName);
  }

  public String getServiceName() {
    return serviceName;
  }

  public PredictorAnnotations setServiceName(String serviceName) {
    this.serviceName = serviceName;
    return this;
  }
}
