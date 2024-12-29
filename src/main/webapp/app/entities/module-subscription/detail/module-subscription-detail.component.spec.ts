import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ModuleSubscriptionDetailComponent } from './module-subscription-detail.component';

describe('ModuleSubscription Management Detail Component', () => {
  let comp: ModuleSubscriptionDetailComponent;
  let fixture: ComponentFixture<ModuleSubscriptionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ModuleSubscriptionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ moduleSubscription: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ModuleSubscriptionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ModuleSubscriptionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load moduleSubscription on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.moduleSubscription).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
