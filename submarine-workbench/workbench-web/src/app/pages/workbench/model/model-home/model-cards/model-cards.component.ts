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

import { Component, Input, OnInit } from '@angular/core';
import { ModelInfo } from '@submarine/interfaces/model-info';
@Component({
  selector: 'submarine-model-cards',
  templateUrl: './model-cards.component.html',
  styleUrls: ['./model-cards.component.scss'],
})
export class ModelCardsComponent implements OnInit {
  @Input() modelCards: Array<ModelInfo>;
  @Input() isLoading: boolean;
  @Input() fetchModelCards;

  nowPage: number;
  totalPages: number;
  onPageModelCards: Array<ModelInfo>;
  pageUnit = 8;

  constructor() {}

  ngOnInit() {
    this.loadOnPageModelCards(1);
  }

  ngOnChanges() {
    this.loadOnPageModelCards(1);
  }

  loadOnPageModelCards = (newPage: number) => {
    this.nowPage = newPage;
    this.totalPages = this.modelCards.length;
    let start = this.pageUnit * (newPage - 1);
    this.onPageModelCards = this.modelCards.filter((_, index) => index < start + this.pageUnit && index >= start);
  }

  refreshCards(refresh: boolean) {
    if (refresh) { this.fetchModelCards(); }
  }
}
