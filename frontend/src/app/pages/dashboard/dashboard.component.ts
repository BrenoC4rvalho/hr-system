import { Component } from '@angular/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { ListBirthdayComponent } from "../../components/list-birthday/list-birthday.component";

@Component({
  selector: 'app-dashboard',
  imports: [NavbarComponent, ListBirthdayComponent],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {

}
