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

package org.apache.submarine.server.rest;

public class RestConstants {
  public static final String V1 = "v1";

  public static final String EXPERIMENT = "experiment";

  public static final String ID = "id";

  public static final String JOB_ID = "JOB_ID";

  public static final String SUBMARINE_TRACKING_URI = "SUBMARINE_TRACKING_URI";

  public static final String PING = "ping";

  public static final String MEDIA_TYPE_YAML = "application/yaml";

  public static final String CHARSET_UTF8 = "charset=utf-8";

  public static final String METASTORE = "metastore";

  public static final String CLUSTER = "cluster";

  public static final String ADDRESS = "address";

  public static final String NODES = "nodes";

  public static final String NODE = "node";

  public static final String LOGS = "logs";
  /**
   * Environment.
   */
  public static final String ENVIRONMENT = "environment";

  public static final String ENVIRONMENT_ID = "id";

  /**
   * Experiment template.
   */
  public static final String EXPERIMENT_TEMPLATES = "template";

  public static final String EXPERIMENT_TEMPLATE_ID = "id";

  public static final String EXPERIMENT_TEMPLATE_SUBMIT = "submit";

  /**
   * Notebook.
   */
  public static final String NOTEBOOK = "notebook";

  public static final String NOTEBOOK_ID = "id";

  /**
   * Tensorboard.
   */
  public static final String LOG_DIR_KEY = "SUBMARINE_TENSORBOARD_LOG_DIR";
  public static final String LOG_DIR_VALUE = "/logs/mylog";

  /**
   * Registered Model.
   */
  public static final String REGISTERED_MODEL = "registered-model";
  public static final String REGISTERED_MODEL_NAME = "name";

  /**
   * Model Version.
   */
  public static final String MODEL_VERSION = "model-version";
  public static final String MODEL_VERSION_NAME = "name";
  public static final String MODEL_VERSION_VERSION = "version";

  /**
   * Serve.
   */
  public static final String SERVE = "serve";

  /**
   *  Internal
   */
  public static final String INTERNAL = "internal";
  public static final String CUSTOM_RESOURCE_TYPE = "customResourceType";
  public static final String CUSTOM_RESOURCE_ID = "resourceId";
  public static final String CUSTOM_RESOURCE_STATUS = "status";
}
