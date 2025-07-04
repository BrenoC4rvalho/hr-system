import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { LucideAngularModule, Eye, UserRoundSearch, Pencil } from 'lucide-angular';
import { Employee } from '../../core/model/employee';
import { EmployeeService } from '../../core/service/employee.service';
import { PaginatedEmployeesResponse } from '../../core/model/paginated-employees-response';
import { PaginationComponent } from "../pagination/pagination.component";
import { Department } from '../../core/model/department';
import { Position } from '../../core/model/position';
import { DepartmentService } from '../../core/service/department.service';
import { PositionService } from '../../core/service/position.service';
import { FormsModule } from '@angular/forms';
import { EmployeeProfileComponent } from "../employee-profile/employee-profile.component";
import { EditEmployeeModalComponent } from "../edit-employee-modal/edit-employee-modal.component";
import { SuccessToastComponent } from "../success-toast/success-toast.component";

@Component({
  selector: 'app-list-employees',
  imports: [CommonModule, LucideAngularModule, PaginationComponent, FormsModule, EmployeeProfileComponent, EditEmployeeModalComponent, SuccessToastComponent],
  templateUrl: './list-employees.component.html',
})
export class ListEmployeesComponent implements OnInit, OnChanges {

  @Input() newEmployee: Employee | undefined;
  @Output() errorMessage = new EventEmitter<string>();

  readonly EyeIcon = Eye;
  readonly UserRoundSearchIcon = UserRoundSearch;
  readonly PencilIcon = Pencil;

  employees: Employee[] = [];
  departments: Department[] = [];
  positions: Position[] = [];

  selectedPosition: Position | null = null;
  selectedDepartment: Department | null = null;
  searchName: string = '';

  currentPage: number = 0;
  pageSize: number = 12;
  totalPages: number = 0;

  showEmployeeProfileModal: boolean = false;
  showEmployeeEditModal: boolean = false;

  selectedEmployeeForModal: Employee | undefined;

  showSuccessToast: boolean = false;
  successMessage: string = '';


  constructor(
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private positionService: PositionService
  ) {}

  ngOnInit(): void {
    this.getEmployees(this.currentPage, this.pageSize);
    this.getDepartments();
    this.getPositions();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(this.newEmployee !== undefined) {
      this.employees.push(this.newEmployee);
      this.newEmployee = undefined;
    }
  }

  getEmployees(page: number, size: number): void {

    const positionId = this.selectedPosition ? this.selectedPosition.id : null;
    const departmentId = this.selectedDepartment ? this.selectedDepartment.id : null;
    const name = this.searchName;

    this.employeeService.getAll(page, size, positionId, departmentId, name).subscribe({
      next: (response: PaginatedEmployeesResponse) => {
        this.employees = response.employees;
        this.currentPage = response.currentPage;
        this.totalPages = response.totalPages;
        this.pageSize = response.pageSize;
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

  getPositions() {

    this.positionService.getAll().subscribe({
      next: (response: Position[]) => {
        response.forEach(position => {
          this.positions.push(position);
        })
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

  getDepartments() {
    this.departmentService.getAll().subscribe({
      next: (response: Department[]) => {
        response.forEach(department => {
          this.departments.push(department);
        })
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

  onPageChange(newPage: number): void {
    this.getEmployees(newPage, this.pageSize);
  }

  onSearch(): void {
    this.getEmployees(0, this.pageSize);
  }

  openEmployeeProfileModal(selectedEmployee: Employee): void {
    this.selectedEmployeeForModal = selectedEmployee;
    this.showEmployeeProfileModal= true;
  }

  closeEmployeeProfileModal(): void {
    this.selectedEmployeeForModal = undefined;
    this.showEmployeeProfileModal = false;
  }

  openEmployeeEditModal(edit: Employee): void {
    this.selectedEmployeeForModal = edit;
    this.showEmployeeEditModal = true;
  }

  closeEmployeeEditModal(): void {
    this.selectedEmployeeForModal = undefined;
    this.showEmployeeEditModal = false;
  }

  handleErrorModal($event: string): void {
    this.errorMessage.emit($event);
  }

  handleEmployeeUpdated(updatedEmployee: Employee): void {
    const index = this.employees.findIndex(emp => emp.id === updatedEmployee.id);
    if (index !== -1) {
      this.employees[index] = updatedEmployee;
      this.successMessage = `Employee '${updatedEmployee.firstName}' updated successfully.`;
      this.showSuccessToast = true;
    }
  }

}
