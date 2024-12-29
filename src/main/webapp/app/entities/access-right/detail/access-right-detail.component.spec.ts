import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccessRightDetailComponent } from './access-right-detail.component';

describe('AccessRight Management Detail Component', () => {
  let comp: AccessRightDetailComponent;
  let fixture: ComponentFixture<AccessRightDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccessRightDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ accessRight: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AccessRightDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AccessRightDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accessRight on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.accessRight).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
