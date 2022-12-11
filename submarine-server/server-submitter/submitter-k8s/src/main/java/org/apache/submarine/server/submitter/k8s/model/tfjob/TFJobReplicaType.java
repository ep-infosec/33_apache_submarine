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

package org.apache.submarine.server.submitter.k8s.model.tfjob;

import com.google.gson.annotations.SerializedName;
import org.apache.submarine.server.submitter.k8s.model.mljob.MLJobReplicaType;

public enum TFJobReplicaType implements MLJobReplicaType {

  @SerializedName("Ps")
  Ps("Ps"),

  @SerializedName("Worker")
  Worker("Worker"),

  @SerializedName("Chief")
  Chief("Chief"),

  @SerializedName("Master")
  Master("Master"),

  @SerializedName("Evaluator")
  Evaluator("Evaluator");


  private String typeName;

  TFJobReplicaType(String n) {
    this.typeName = n;
  }

  public static boolean isSupportedReplicaType(String type) {
    return type.equalsIgnoreCase("Ps") ||
        type.equalsIgnoreCase("Worker") ||
        type.equalsIgnoreCase("Chief") ||
        type.equalsIgnoreCase("Master") ||
        type.equalsIgnoreCase("Evaluator");
  }

  public static String[] names() {
    TFJobReplicaType[] types = values();
    String[] names = new String[types.length];
    for (int i = 0; i < types.length; i++) {
      names[i] = types[i].name();
    }
    return names;
  }
  @Override
  public String getTypeName() {
    return this.typeName;
  }
}
