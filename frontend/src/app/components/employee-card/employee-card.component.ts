import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { RouterModule } from '@angular/router';
import { EmployeeService } from '../../core/service/employee.service';
import { EmployeeStatusSummary } from '../../core/model/employee-status-summary';

@Component({
  selector: 'app-employee-card',
  imports: [ RouterModule ],
  templateUrl: './employee-card.component.html',
})
export class EmployeeCardComponent implements OnInit {

  @Output() errorMessage = new EventEmitter<string>();

  totalEmployees: number = 0;

  totalEmployeesActive: number = 0;
  totalEmployeesOnLeave: number = 0;
  totalEmployeesSickLeave: number = 0;
  totalEmployeesLeaveOfAbsence: number = 0;

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.getStatusSummary();
  }

  getStatusSummary(): void {
    this.employeeService.getEmployeeStatusSummary().subscribe({
      next: (response: EmployeeStatusSummary) => {
        console.log(response)
        this.totalEmployees = response.ACTIVE + response.ON_LEAVE + response.SICK_LEAVE + response.LEAVE_OF_ABSENCE;
        this.totalEmployeesActive = response.ACTIVE;
        this.totalEmployeesOnLeave = response.ON_LEAVE;
        this.totalEmployeesSickLeave = response.SICK_LEAVE;
        this.totalEmployeesLeaveOfAbsence = response.LEAVE_OF_ABSENCE;
      },
      error: (error) => {
        if(error && error.error) {
          this.errorMessage.emit(error.error);
        } else {
          this.errorMessage.emit('An unexpected error occurred. Please try again later.');
        }
      }
    })
  }


}
