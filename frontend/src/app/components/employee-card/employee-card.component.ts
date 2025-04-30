import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-employee-card',
  imports: [ RouterModule ],
  templateUrl: './employee-card.component.html',
})
export class EmployeeCardComponent {

  totalEmployees: number = 20;

  totalEmployeesActive: number = 10;
  totalEmployeesOnLeave: number = 2;
  totalEmployeesSickLeave: number = 4;
  totalEmployeesLeaveOfAbsence: number = 4;

}
