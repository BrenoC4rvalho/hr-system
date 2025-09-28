import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ModalComponent } from "../../shared/modal/modal.component";
import { Department } from '../../core/model/department';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Field } from '../../core/types/Field';
import { DepartmentService } from '../../core/service/department.service';
import { CommonModule } from '@angular/common';
import { DynamicFieldComponent } from '../dynamic-field/dynamic-field.component';
import { Employee } from '../../core/model/employee';
import { EmployeeBasic } from '../../core/model/employee-basic';
import { ListEmployeeSearchComponent } from "../list-employee-search/list-employee-search.component";

@Component({
  selector: 'app-edit-department-modal',
  imports: [ModalComponent, CommonModule, ReactiveFormsModule, DynamicFieldComponent, ListEmployeeSearchComponent],
  templateUrl: './edit-department-modal.component.html',
})
export class EditDepartmentModalComponent implements OnInit {

  @Input() department: Department | undefined;
  @Output() closeModal = new EventEmitter<void>();
  @Output() errorMessage = new EventEmitter<string>();
  @Output() editDepartment = new EventEmitter<Department>()

  form!: FormGroup;
  inputGroups: Field[][] = [];
  selectedManagerName: string = '';

  constructor(
    private fb: FormBuilder,
    private departmentService: DepartmentService
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      name: [this.department?.name || '', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      manager: [this.department?.manager || null]
    });

    this.inputGroups = [
      [
        {
          label: 'Name',
          name: 'name',
          type: 'input',
          inputType: 'text',
          placeholder: 'Mechanic',
          value: this.department?.name || ''
        }
      ]
    ];

    if (this.department?.manager) {
      this.selectedManagerName = `${this.department.manager.firstName} ${this.department.manager.lastName}`;
    }
  }

  onClose(): void {
    this.closeModal.emit();
  }

  onSave():void {

    if(this.form.invalid) {
          this.errorMessage.emit('Fill in all required fields.')
          return
    }

    const editDepartment: Department = this.form.value;

    if (this.department?.id) {
      this.departmentService.update(this.department.id, editDepartment).subscribe({
        next: (updatedDepartment) => {
          this.editDepartment.emit(updatedDepartment);
          this.closeModal.emit();
        },
        error: (err) => {
          const errorMsg = err?.error || 'Failed to update department.';
          this.errorMessage.emit(errorMsg);
        }
      });
    }

  }

  onEmployeeSelected(employee: EmployeeBasic): void {
    this.form.patchValue({ manager: employee });
    this.selectedManagerName = `${employee.firstName} ${employee.lastName}`;
  }


}
