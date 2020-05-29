import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewSecretComponent } from './new-secret.component';

describe('NewSecretComponent', () => {
  let component: NewSecretComponent;
  let fixture: ComponentFixture<NewSecretComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewSecretComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewSecretComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
