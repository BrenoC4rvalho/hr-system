import { Component } from '@angular/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { ListBirthdayComponent } from "../../components/list-birthday/list-birthday.component";
import { DepartmentCardComponent } from "../../components/department-card/department-card.component";
import { ErrorModalComponent } from "../../components/error-modal/error-modal";
import { ShiftCardComponent } from "../../components/shift-card/shift-card.component";
import { EmployeeCardComponent } from "../../components/employee-card/employee-card.component";

@Component({
  selector: 'app-dashboard',
  imports: [NavbarComponent, ListBirthdayComponent, DepartmentCardComponent, ErrorModalComponent, ShiftCardComponent, EmployeeCardComponent],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {

  showErrorModal: boolean = false;
  errorMessage: string = ''

  handleErrorModal($event: string) {
    this.showErrorModal = true;
    this.errorMessage = $event;
  }

}
