import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { ModalComponent } from "../../shared/modal/modal.component";
import { Department } from '../../core/model/department';
import { CommonModule } from '@angular/common';
import { DepartmentService } from '../../core/service/department.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-department-profile-modal',
  imports: [ModalComponent, CommonModule, FormsModule],
  templateUrl: './department-profile-modal.component.html',
})
export class DepartmentProfileModalComponent implements OnChanges {

  @Input() departmentId: number | undefined;
  @Output() closeModal = new EventEmitter<void>();
  @Output() errorMessage = new EventEmitter<string>();

  department: Department | null = null;
  editableDepartment: Department = {} as Department;
  isEditing = false;

  constructor(private departmentService: DepartmentService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['departmentId'] && this.departmentId) {
      this.loadDepartment();
    }
  }

  loadDepartment(): void {
    if (!this.departmentId) return;
    this.departmentService.show(this.departmentId).subscribe({
      next: (department: Department) => {
        this.department = department;
        this.editableDepartment = { ...department };
      },
      error: () => {
        this.errorMessage.emit('Failed to load department');
      }
    });
  }

  toggleEdit(): void {
    this.isEditing = true;
  }

  cancelEdit(): void {
    if (this.department) {
      this.editableDepartment = { ...this.department };
    }
    this.isEditing = false;
  }

  saveChanges(): void {
    if (!this.departmentId) return;

    this.departmentService.update(this.departmentId, this.editableDepartment).subscribe({
      next: (updatedDept) => {
        this.department = updatedDept;
        this.isEditing = false;
      },
      error: () => {
        this.errorMessage.emit('Failed to save changes.');
      }
    });
  }

  onClose(): void {
    this.closeModal.emit();
  }
}
