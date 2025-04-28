import { Component } from '@angular/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { ListBirthdayComponent } from "../../components/list-birthday/list-birthday.component";
import { DepartmentCardComponent } from "../../components/department-card/department-card.component";

@Component({
  selector: 'app-dashboard',
  imports: [NavbarComponent, ListBirthdayComponent, DepartmentCardComponent],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {

}
