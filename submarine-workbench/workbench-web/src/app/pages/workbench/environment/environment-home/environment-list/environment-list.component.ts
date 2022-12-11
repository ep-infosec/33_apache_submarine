/*!
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

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ExperimentInfo } from '@submarine/interfaces/experiment-info';

@Component({
  selector: 'submarine-environment-list',
  templateUrl: './environment-list.component.html',
  styleUrls: ['./environment-list.component.scss'],
})
export class EnvironmentListComponent implements OnInit {
  @Input() environmentList: ExperimentInfo[];
  @Output() deleteEnvironment = new EventEmitter<string>();

  constructor() {}

  ngOnInit() {}

  onDeleteEnvironment(name: string) {
    this.deleteEnvironment.emit(name);
  }

  onDownloadEnvironmentSpec = (data) => {
    var spec = 'data:text/json;charset=utf-8,' + encodeURIComponent(JSON.stringify(data, null, '\t'));
    var downloader = document.createElement('a');
    downloader.setAttribute('href', spec);
    downloader.setAttribute('download', 'environmentSpec.json');
    downloader.click();
  };
}
