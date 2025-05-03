import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { EmployeeBasic } from '../../core/model/employee-basic';
import { EmployeeService } from '../../core/service/employee.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-list-employee-search',
  imports: [ CommonModule, ReactiveFormsModule ],
  templateUrl: './list-employee-search.component.html',
})
export class ListEmployeeSearchComponent implements OnChanges {

  @Input() firstName: string = '';
  @Input() departmentId?: number;
  @Output() employeeSelected = new EventEmitter<EmployeeBasic>();
  @Output() errorMessage = new EventEmitter<string>();

  employees: EmployeeBasic[] = [];

  constructor(private employeeService: EmployeeService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['firstName'] || changes['departmentId']) {
      this.getEmployees(this.firstName, this.departmentId);
    }
  }


  getEmployees(firstName: string, departmentId?: number): void {

    if(firstName.trim() === '' && !departmentId) {
      return;
    }

    this.employeeService.getEmployeeByName(firstName, departmentId).subscribe({
      next: (response: EmployeeBasic[]) => {
        this.employees = response;
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
