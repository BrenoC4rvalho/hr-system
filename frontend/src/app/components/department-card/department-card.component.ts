import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Department } from '../../core/model/department';
import { DepartmentService } from '../../core/service/department.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-department-card',
  imports: [CommonModule],
  templateUrl: './department-card.component.html',
})
export class DepartmentCardComponent implements OnInit{

  @Output() errorMessage = new EventEmitter<string>()

  departments: Department[] = []
  totalDepartments: number = 0;

  constructor(private departmentService: DepartmentService) {}

  ngOnInit(): void {
    this.getDepartments();
    this.totalDepartments = this.departments.length;
  }

  getDepartments(): void {
    this.departmentService.getAll().subscribe({
      next: (response: Department[]) => {
        this.departments = response;
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
