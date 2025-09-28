import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { EmployeeBasic } from '../../core/model/employee-basic';
import { EmployeeService } from '../../core/service/employee.service';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LucideAngularModule, UserRoundSearch } from 'lucide-angular';

@Component({
  selector: 'app-list-employee-search',
  imports: [ CommonModule, ReactiveFormsModule, LucideAngularModule, FormsModule ],
  templateUrl: './list-employee-search.component.html',
})
export class ListEmployeeSearchComponent {

  readonly UserRoundSearchIcon = UserRoundSearch;

  @Input() departmentId?: number;
  @Output() employeeSelected = new EventEmitter<EmployeeBasic>();
  @Output() errorMessage = new EventEmitter<string>();

  searchText: string = '';
  showDropdown: boolean = false;
  employees: EmployeeBasic[] = [];

  constructor(private employeeService: EmployeeService) {}

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

  onSearch(): void {
    if (this.searchText.trim().length === 0) {
      this.employees = [];
      return;
    }

    this.getEmployees(this.searchText, this.departmentId);
  }

  selectEmployee(employee: EmployeeBasic): void {
    this.searchText = `${employee.firstName} ${employee.lastName}`;
    this.employeeSelected.emit(employee);
    this.showDropdown = false;
  }

  hideDropdown() {
    setTimeout(() => this.showDropdown = false, 150);
  }


}
