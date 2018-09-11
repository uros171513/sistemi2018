import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SymptomnewComponent } from './symptomnew.component';

describe('SymptomnewComponent', () => {
  let component: SymptomnewComponent;
  let fixture: ComponentFixture<SymptomnewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SymptomnewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SymptomnewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
