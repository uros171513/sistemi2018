import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MngdiseasesComponent } from './mngdiseases.component';

describe('MngdiseasesComponent', () => {
  let component: MngdiseasesComponent;
  let fixture: ComponentFixture<MngdiseasesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MngdiseasesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MngdiseasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
