import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { Department } from '../../core/model/department';
import { DepartmentService } from '../../core/service/department.service';
import { CommonModule } from '@angular/common';
import { Eye, LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-departments',
  imports: [NavbarComponent, CommonModule, LucideAngularModule],
  templateUrl: './departments.component.html',
})
export class DepartmentsComponent implements OnInit {

  readonly EyeIcon = Eye;

  showErrorModal: boolean = false;
  errorMessage: string = '';

  departments: Department[] = [];

  constructor(private departmentService: DepartmentService) {}

  ngOnInit(): void {
    this.getDepartments();
  }

  getDepartments(): void {
    this.departmentService.getAll().subscribe({
      next: (response: Department[]) => {
        this.departments = response;
      },
      error: (error) => {
        if(error && error.error) {
          this.showErrorModal = true;
          this.errorMessage = error.error
        } else {
          this.showErrorModal = true;
          this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
      }
    })
  }
}
