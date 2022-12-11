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

package org.apache.submarine.server.database.experiment;

import org.apache.submarine.server.database.experiment.entity.ExperimentEntity;
import org.apache.submarine.server.database.experiment.service.ExperimentService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ExperimentServiceTest {
  private static final Logger LOG = LoggerFactory.getLogger(ExperimentServiceTest.class);
  ExperimentService experimentService = new ExperimentService();

  @After
  public void cleanExperimentTable() throws Exception {
    List<ExperimentEntity> entities = experimentService.selectAll();
    for (ExperimentEntity entity: entities) {
      experimentService.delete(entity.getId());
    }
  }

  @Test
  public void testInsert() throws Exception {
    ExperimentEntity entity = new ExperimentEntity();
    String id = "experiment_1230";
    String spec = "{\"value\": 1}";

    entity.setId(id);
    entity.setExperimentSpec(spec);

    experimentService.insert(entity);

    ExperimentEntity entitySelected = experimentService.select(id);

    compareEntity(entity, entitySelected);
  }

  @Test
  public void testSelectAll() throws Exception  {
    final int SIZE = 3;
    List<ExperimentEntity> entities = new ArrayList<ExperimentEntity>();

    for (int i = 0; i < SIZE; i++) {
      ExperimentEntity entity = new ExperimentEntity();
      entity.setId(String.format("experiment_%d", i));
      entity.setExperimentSpec(String.format("{\"value\": %d}", i));
      experimentService.insert(entity);
      entities.add(entity);
    }

    List<ExperimentEntity> entities_selected = experimentService.selectAll();

    Assert.assertEquals(SIZE, entities_selected.size());
    for (int i = 0; i < entities_selected.size(); i++) {
      compareEntity(entities.get(i), entities_selected.get(i));
    }
  };

  @Test
  public void testUpdate() throws Exception {
    ExperimentEntity entity = new ExperimentEntity();
    String id = "experiment_1230";
    String spec = "{\"value\": 1}";
    entity.setId(id);
    entity.setExperimentStatus("running");
    entity.setExperimentSpec(spec);
    experimentService.insert(entity);

    String new_spec = "{\"value\": 2}";
    entity.setExperimentStatus("complete");
    entity.setExperimentSpec(new_spec);
    experimentService.update(entity);

    ExperimentEntity entitySelected = experimentService.select(id);
    compareEntity(entity, entitySelected);
  };

  @Test
  public void testDelete() throws Exception {
    ExperimentEntity entity = new ExperimentEntity();
    String id = "experiment_1230";
    String spec = "{\"value\": 1}";

    entity.setId(id);
    entity.setExperimentSpec(spec);

    experimentService.insert(entity);

    experimentService.delete(id);

    List<ExperimentEntity> entitySelected = experimentService.selectAll();

    Assert.assertEquals(0, entitySelected.size());
  };

  private void compareEntity(ExperimentEntity expected, ExperimentEntity actual) {
    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getExperimentSpec(), actual.getExperimentSpec());
    Assert.assertEquals(expected.getExperimentStatus(), actual.getExperimentStatus());
  }
}
