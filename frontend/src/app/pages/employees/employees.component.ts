import { Component } from '@angular/core';
import { ListEmployeesComponent } from "../../components/list-employees/list-employees.component";
import { EmployeeProfileComponent } from "../../components/employee-profile/employee-profile.component";
import { NavbarComponent } from '../../shared/navbar/navbar.component';

@Component({
  selector: 'app-employees',
  imports: [ListEmployeesComponent, EmployeeProfileComponent, NavbarComponent],
  templateUrl: './employees.component.html',
})
export class EmployeesComponent {



}
