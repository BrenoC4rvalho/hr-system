import { Component } from '@angular/core';
import { ListEmployeesComponent } from "../../components/list-employees/list-employees.component";

@Component({
  selector: 'app-employees',
  imports: [ListEmployeesComponent],
  templateUrl: './employees.component.html',
})
export class EmployeesComponent {

}
