import { Component } from '@angular/core';
import { ListEmployeesComponent } from "../../components/list-employees/list-employees.component";
import { EmployeeProfileComponent } from "../../components/employee-profile/employee-profile.component";
import { NavbarComponent } from '../../shared/navbar/navbar.component';
import { Employee } from '../../core/model/employee';
import { NewEmployeeModalComponent } from "../../components/new-employee-modal/new-employee-modal.component";
import { CommonModule } from '@angular/common';
import { ErrorModalComponent } from "../../components/error-modal/error-modal";

@Component({
  selector: 'app-employees',
  imports: [ListEmployeesComponent, NavbarComponent, NewEmployeeModalComponent, CommonModule, ErrorModalComponent],
  templateUrl: './employees.component.html',
})
export class EmployeesComponent {

  showErrorModal: boolean = false;
  errorMessage: string = ''

  newEmployee: Employee | undefined;

  isModalNewEmployeeOpen: boolean = false;

  openModalNewEmployee() {
    this.isModalNewEmployeeOpen = true;
  }

  closeModalNewEmployee() {
    this.isModalNewEmployeeOpen = false;
  }

  onEmployeeCreated($event: Employee) {
    this.newEmployee = $event;
  }

  handleErrorModal($event: string) {
    this.showErrorModal = true;
    this.errorMessage = $event;
  }

}
