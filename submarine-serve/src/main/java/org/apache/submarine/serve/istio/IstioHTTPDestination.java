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
package org.apache.submarine.serve.istio;

import com.google.gson.annotations.SerializedName;
import org.apache.submarine.serve.utils.IstioConstants;

public class IstioHTTPDestination {

  @SerializedName("destination")
  private IstioDestination destination;

  public IstioHTTPDestination() {
  }

  public IstioHTTPDestination(String host) {
    this.destination = new IstioDestination(host);
  }

  public IstioHTTPDestination(String host, Integer port) {
    this.destination = new IstioDestination(host, port);
  }

  public static class IstioDestination {

    @SerializedName("host")
    private String host;

    @SerializedName("port")
    private IstioPort port;

    public IstioDestination() {
    }

    public IstioDestination(String host) {
      this(host, IstioConstants.DEFAULT_SERVE_POD_PORT);
    }

    public IstioDestination(String host, Integer port) {
      this.host = host;
      this.port = new IstioPort(port);
    }

    public String getHost() {
      return host;
    }

    public void setHost(String host) {
      this.host = host;
    }

    public IstioPort getPort() {
      return port;
    }

    public void setPort(IstioPort port) {
      this.port = port;
    }
  }

  public static class IstioPort {

    @SerializedName("number")
    private Integer number;

    public IstioPort() {
    }

    public IstioPort(Integer port) {
      this.number = port;
    }

    public Integer getNumber() {
      return number;
    }

    public void setNumber(Integer number) {
      this.number = number;
    }
  }

  public IstioDestination getDestination() {
    return destination;
  }

  public void setDestination(IstioDestination destination) {
    this.destination = destination;
  }
}
