import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ModalComponent } from "../../shared/modal/modal.component";
import { Employee } from '../../core/model/employee';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Field } from '../../core/types/Field';
import { EmployeeService } from '../../core/service/employee.service';
import { DynamicFieldComponent } from "../dynamic-field/dynamic-field.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-employee-modal',
  imports: [ModalComponent, DynamicFieldComponent, CommonModule, ReactiveFormsModule],
  templateUrl: './edit-employee-modal.component.html',
})
export class EditEmployeeModalComponent {


  @Input() employee: Employee | undefined;
  @Output() closeModal = new EventEmitter<void>();
  @Output() errorMessage = new EventEmitter<string>();
  @Output() editEmployee = new EventEmitter<Employee>()

  form!: FormGroup;

  inputGroups: Field[][] = [];

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
    })
  }

  onClose(): void {
    this.closeModal.emit();
  }

  onSave():void {

    if(this.form.invalid) {
          this.errorMessage.emit('Fill in all required fields.')
          return
    }

    const editEmployee: Employee = this.form.value;

  }

}
