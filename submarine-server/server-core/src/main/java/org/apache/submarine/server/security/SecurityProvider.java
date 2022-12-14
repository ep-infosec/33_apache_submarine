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

package org.apache.submarine.server.security;

import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Provide security methods for different authentication types
 */
public interface SecurityProvider<T extends Filter, R extends CommonProfile> {

  String DEFAULT_AUTHORIZER = "isAuthenticated";

  /**
   * Get filter class
   */
  Class<T> getFilterClass();

  /**
   * Get pac4j config
   */
  Config getConfig();

  /**
   * Get pac4j client
   */
  String getClient(HttpServletRequest httpServletRequest);

  /**
   * Process authentication information and return user profile
   */
  Optional<R> perform(HttpServletRequest hsRequest, HttpServletResponse hsResponse);

  /**
   * Get user profile
   */
  Optional<R> getProfile(HttpServletRequest hsRequest, HttpServletResponse hsResponse);
}
