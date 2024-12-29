import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IModuleSubscription } from '../module-subscription.model';

@Component({
  selector: 'jhi-module-subscription-detail',
  templateUrl: './module-subscription-detail.component.html',
})
export class ModuleSubscriptionDetailComponent implements OnInit {
  moduleSubscription: IModuleSubscription | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moduleSubscription }) => {
      this.moduleSubscription = moduleSubscription;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
