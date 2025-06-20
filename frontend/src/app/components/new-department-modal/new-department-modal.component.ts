import { Component, EventEmitter, Output } from '@angular/core';
import { CircleX, LucideAngularModule } from 'lucide-angular';
import { DepartmentService } from '../../core/service/department.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Department } from '../../core/model/department';
import { ModalComponent } from "../../shared/modal/modal.component";
import { CreateDepartment } from '../../core/model/create-department';
import { DynamicFieldComponent } from "../dynamic-field/dynamic-field.component";
import { Field } from '../../core/types/Field';

@Component({
  selector: 'app-new-department-modal',
  imports: [LucideAngularModule, ReactiveFormsModule, CommonModule, ModalComponent, DynamicFieldComponent],
  templateUrl: './new-department-modal.component.html',
})
export class NewDepartmentModalComponent {

  readonly CircleXIcon = CircleX;

  @Output() closeModal = new EventEmitter<void>()
  @Output() createdDepartment = new EventEmitter<Department>()
  @Output() errorMessage = new EventEmitter<string>()

  form: FormGroup;

  inputGroups: Field[][] = [
    [
      { label: 'Name', name: 'name', type: 'input', inputType: 'text', placeholder: 'Mechanic' }
    ]
  ]

  constructor(
    private fb: FormBuilder,
    private departmentService: DepartmentService
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]]
    })
  }

  onSave(): void {

    if(this.form.invalid) {
      this.errorMessage.emit('Fill in all required fields.')
      return
    }

    const newDepartment: CreateDepartment = this.form.value;

    this.departmentService.create(newDepartment).subscribe({
      next: (response: Department) => {
        this.createdDepartment.emit(response);
        this.closeModal.emit();
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

  onClose() {
    this.closeModal.emit();
  }

}
