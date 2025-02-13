import { Component } from '@angular/core';
import { ListEmployeesComponent } from "../../components/list-employees/list-employees.component";
import { EmployeeProfileComponent } from "../../components/employee-profile/employee-profile.component";

@Component({
  selector: 'app-employees',
  imports: [ListEmployeesComponent, EmployeeProfileComponent],
  templateUrl: './employees.component.html',
})
export class EmployeesComponent {



}
