import { Component, EventEmitter, Output } from '@angular/core';
import { CircleX, LucideAngularModule } from 'lucide-angular';
import { DepartmentService } from '../../core/service/department.service';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Department } from '../../core/model/department';

@Component({
  selector: 'app-new-department-modal',
  imports: [LucideAngularModule, FormsModule, CommonModule],
  templateUrl: './new-department-modal.component.html',
})
export class NewDepartmentModalComponent {

  readonly CircleXIcon = CircleX;

  @Output() closeModal = new EventEmitter<void>()
  @Output() createdDepartment = new EventEmitter<Department>()

  name: string = ''

  constructor(private departmentService: DepartmentService) {}

  onSubmit(): void {
    this.departmentService.create({ name: this.name }).subscribe({
      next: (response: Department) => {
        this.createdDepartment.emit(response);
        this.closeModal.emit();
      },
      error: (error) => {

      }
    })
  }

  onClose() {
    this.closeModal.emit();
  }

}
