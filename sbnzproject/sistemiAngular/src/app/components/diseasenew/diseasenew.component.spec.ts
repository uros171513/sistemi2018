import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiseasenewComponent } from './diseasenew.component';

describe('DiseasenewComponent', () => {
  let component: DiseasenewComponent;
  let fixture: ComponentFixture<DiseasenewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiseasenewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiseasenewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
