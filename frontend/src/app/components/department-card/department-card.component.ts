import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Department } from '../../core/model/department';
import { DepartmentService } from '../../core/service/department.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-department-card',
  imports: [CommonModule, RouterModule],
  templateUrl: './department-card.component.html',
})
export class DepartmentCardComponent implements OnInit{

  @Output() errorMessage = new EventEmitter<string>()

  departments: Department[] = []
  totalDepartments: number = 0;
  totalEmployees: number = 0;

  constructor(private departmentService: DepartmentService) {}

  ngOnInit(): void {
    this.getDepartments();
  }

  getDepartments(): void {
    this.departmentService.getAll().subscribe({
      next: (response: Department[]) => {
        this.departments = response;
        this.totalDepartments = this.departments.length;
        this.totalEmployees = response.reduce((sum, department) => sum + department.numberOfEmployees, 0);
        console.log(this.totalEmployees)
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
