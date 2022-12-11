/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.submarine.server.utils.response;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.submarine.server.utils.response.JsonResponse.ListResult;
import org.apache.submarine.server.database.workbench.entity.SysDictEntity;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class JsonResponseTest {
  private GsonBuilder gsonBuilder = new GsonBuilder();
  private Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();

  @Test
  public void serializeObject() {
    SysDictEntity sysDict = new SysDictEntity();
    sysDict.setDictCode("code");
    sysDict.setDictName("name");
    sysDict.setDescription("desc");

    Response response = new JsonResponse.Builder<SysDictEntity>(Response.Status.OK)
        .success(true).result(sysDict).build();

    String entity = (String) response.getEntity();

    Type type = new TypeToken<JsonResponse<SysDictEntity>>() {}.getType();

    JsonResponse<SysDictEntity> jsonResponse = gson.fromJson(entity, type);

    SysDictEntity checkDict = jsonResponse.getResult();
    assertEquals(checkDict.getDictCode(), "code");
    assertEquals(checkDict.getDictName(), "name");
    assertEquals(checkDict.getDescription(), "desc");
  }

  @Test
  public void serializeListResult() {
    SysDictEntity sysDict = new SysDictEntity();
    sysDict.setDictCode("code");
    sysDict.setDictName("name");
    sysDict.setDescription("desc");

    List<SysDictEntity> list = new ArrayList();
    list.add(sysDict);

    ListResult<SysDictEntity> listResult = new ListResult(list, list.size());

    Response response = new JsonResponse.Builder<ListResult<SysDictEntity>>(Response.Status.OK)
        .success(true).result(listResult).build();

    String entity = (String) response.getEntity();

    Type type = new TypeToken<JsonResponse<ListResult<SysDictEntity>>>() {}.getType();

    JsonResponse<ListResult<SysDictEntity>> jsonResponse = gson.fromJson(entity, type);

    ListResult<SysDictEntity> check = jsonResponse.getResult();
    assertTrue(check.getRecords().get(0) instanceof SysDictEntity);
    assertEquals(check.getRecords().size(), listResult.getRecords().size());
    assertEquals(check.getTotal(), listResult.getTotal());
    assertEquals(check.getRecords().get(0).getDictCode(), sysDict.getDictCode());
    assertEquals(check.getRecords().get(0).getDictName(), sysDict.getDictName());
    assertEquals(check.getRecords().get(0).getDescription(), sysDict.getDescription());
  }
}
