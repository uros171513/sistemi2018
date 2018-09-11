import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MngdoctorsComponent } from './mngdoctors.component';

describe('MngdoctorsComponent', () => {
  let component: MngdoctorsComponent;
  let fixture: ComponentFixture<MngdoctorsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MngdoctorsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MngdoctorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
