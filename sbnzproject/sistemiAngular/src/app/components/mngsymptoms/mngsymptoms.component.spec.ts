import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MngsymptomsComponent } from './mngsymptoms.component';

describe('MngsymptomsComponent', () => {
  let component: MngsymptomsComponent;
  let fixture: ComponentFixture<MngsymptomsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MngsymptomsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MngsymptomsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
