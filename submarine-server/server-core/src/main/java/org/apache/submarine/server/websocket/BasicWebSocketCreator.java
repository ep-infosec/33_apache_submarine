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
package org.apache.submarine.server.websocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible to create the WebSockets for WebSocketServer.
 */
public class BasicWebSocketCreator implements WebSocketCreator {
  private static final Logger LOG = LoggerFactory.getLogger(BasicWebSocketCreator.class);
  private WebSocketServer webSocketServer;

  public BasicWebSocketCreator(WebSocketServer webSocketServer) {
    this.webSocketServer = webSocketServer;
  }
  public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
    return new WebSocketHandler(request.getHttpServletRequest(), "", webSocketServer);
  }

}
