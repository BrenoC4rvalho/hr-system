import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { LucideAngularModule, Eye, UserRoundSearch } from 'lucide-angular';
import { Employee } from '../../core/model/employee';
import { EmployeeService } from '../../core/service/employee.service';
import { PaginatedEmployeesResponse } from '../../core/model/paginated-employees-response';
import { PaginationComponent } from "../pagination/pagination.component";

@Component({
  selector: 'app-list-employees',
  imports: [CommonModule, LucideAngularModule, PaginationComponent],
  templateUrl: './list-employees.component.html',
})
export class ListEmployeesComponent implements OnInit, OnChanges {

  @Input() newEmployee: Employee | undefined;

  readonly EyeIcon = Eye;
  readonly UserRoundSearchIcon = UserRoundSearch;

  employees: Employee[] = [];

  currentPage: number = 0;
  pageSize: number = 12;
  totalPages: number = 0;

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.getEmployees(this.currentPage, this.pageSize)
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(this.newEmployee !== undefined) {
      this.employees.push(this.newEmployee);
      this.newEmployee = undefined;
    }
  }

  getEmployees(page: number, size: number): void {
    this.employeeService.getAll(page, size).subscribe({
      next: (response: PaginatedEmployeesResponse) => {
        this.employees = response.employees;
        this.currentPage = response.currentPage;
        this.totalPages = response.totalPages;
        this.pageSize = response.pageSize;
      },
      error: (error) => {

      }
    })
  }

  onPageChange(newPage: number): void {
    this.getEmployees(newPage, this.pageSize);
  }

}
