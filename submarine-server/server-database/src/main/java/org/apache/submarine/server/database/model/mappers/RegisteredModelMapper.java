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

package org.apache.submarine.server.database.model.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.submarine.server.database.model.entities.RegisteredModelEntity;

public interface RegisteredModelMapper {
  List<RegisteredModelEntity> selectAll();
  RegisteredModelEntity select(String name);
  RegisteredModelEntity selectWithTag(String name);

  void insert(RegisteredModelEntity registeredModel);
  void update(RegisteredModelEntity registeredModel);
  void rename(@Param("name")String name, @Param("newName")String newName);
  void delete(String name);
  void deleteAll();
}
