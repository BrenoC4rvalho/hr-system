import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { Department } from '../../core/model/department';
import { DepartmentService } from '../../core/service/department.service';
import { CommonModule } from '@angular/common';
import { Eye, LucideAngularModule } from 'lucide-angular';
import { NewDepartmentModalComponent } from "../../components/new-department-modal/new-department-modal.component";
import { ErrorModalComponent } from "../../components/error-modal/error-modal";
import { DepartmentProfileModalComponent } from "../../components/department-profile-modal/department-profile-modal.component";

@Component({
  selector: 'app-departments',
  imports: [NavbarComponent, CommonModule, LucideAngularModule, NewDepartmentModalComponent, ErrorModalComponent, DepartmentProfileModalComponent],
  templateUrl: './departments.component.html',
})
export class DepartmentsComponent implements OnInit {

  readonly EyeIcon = Eye;

  isModalNewDepartmentOpen: boolean = false;

  showErrorModal: boolean = false;
  errorMessage: string = '';

  departments: Department[] = [];

  selectedDepartmentForModal: Department | null = null;
  showDepartmentProfileModal: boolean = false;

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

  openModalNewDepartment() {
    this.isModalNewDepartmentOpen = true;
  }

  closeModalNewDepartment() {
    this.isModalNewDepartmentOpen = false
  }

  onDepartmentCreated($event: Department) {
    this.departments.push($event);
  }

  handleErrorModal($event: string) {
    this.showErrorModal = true;
    this.errorMessage = $event;
  }

  openDepartmentProfileModal(selectedDepartment: Department): void {
    this.selectedDepartmentForModal = selectedDepartment;
    this.showDepartmentProfileModal = true;
  }

  closeDepartmentProfileModal(): void {
    this.showDepartmentProfileModal = false;
    this.selectedDepartmentForModal = null;
  }


}
