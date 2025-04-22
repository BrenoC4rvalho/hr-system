import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListBirthdayComponent } from './list-birthday.component';

describe('ListBirthdayComponent', () => {
  let component: ListBirthdayComponent;
  let fixture: ComponentFixture<ListBirthdayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListBirthdayComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListBirthdayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
