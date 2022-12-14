/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.submarine.integration;

import org.apache.submarine.AbstractSubmarineIT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import java.io.File;

public class SubmarineLogTest extends AbstractSubmarineIT {
  public final static Logger LOG = LoggerFactory.getLogger(SubmarineLogTest.class);

  @Test
  public void submarineLog() throws Exception {
    LOG.info("[Testcase]: SubmarineLogIT");
    printSubmarineLog();
  }

  @Test
  public void listSubmarineLibFiles() throws Exception {
    File directory = new File(".");
    String submarineDistModulePath = directory.getCanonicalPath() + "/../../submarine-dist/";
    LOG.info("submarine-dist module path = {}", submarineDistModulePath);

    listTargetDirFiles(new File(submarineDistModulePath), "lib");
  }
}
