import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IModuleSubscription } from '../module-subscription.model';
import { ModuleSubscriptionService } from '../service/module-subscription.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './module-subscription-delete-dialog.component.html',
})
export class ModuleSubscriptionDeleteDialogComponent {
  moduleSubscription?: IModuleSubscription;

  constructor(protected moduleSubscriptionService: ModuleSubscriptionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.moduleSubscriptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
