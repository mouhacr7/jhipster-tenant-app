import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccessRight } from '../access-right.model';

@Component({
  selector: 'jhi-access-right-detail',
  templateUrl: './access-right-detail.component.html',
})
export class AccessRightDetailComponent implements OnInit {
  accessRight: IAccessRight | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accessRight }) => {
      this.accessRight = accessRight;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
