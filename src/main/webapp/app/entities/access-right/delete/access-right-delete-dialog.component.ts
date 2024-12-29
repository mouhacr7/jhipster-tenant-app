import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccessRight } from '../access-right.model';
import { AccessRightService } from '../service/access-right.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './access-right-delete-dialog.component.html',
})
export class AccessRightDeleteDialogComponent {
  accessRight?: IAccessRight;

  constructor(protected accessRightService: AccessRightService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accessRightService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
