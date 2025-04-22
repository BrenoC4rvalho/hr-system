import { Component, EventEmitter, Output } from '@angular/core';
import { Employee } from '../../core/model/employee';
import { EmployeeService } from '../../core/service/employee.service';
import { CreateEmployee } from '../../core/model/create-employee';
import { Shift } from '../../core/enums/shift.enum';
import { Gender } from '../../core/enums/gender.enum';
import { Department } from '../../core/model/department';
import { Position } from '../../core/model/position';
import { DepartmentService } from '../../core/service/department.service';
import { PositionService } from '../../core/service/position.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ModalComponent } from "../../shared/modal/modal.component";
import { Field } from '../../core/types/Field';
import { DynamicFieldComponent } from '../dynamic-field/dynamic-field.component';

@Component({
  selector: 'app-new-employee-modal',
  imports: [CommonModule, ReactiveFormsModule, ModalComponent, DynamicFieldComponent],
  templateUrl: './new-employee-modal.component.html',
})
export class NewEmployeeModalComponent {

  @Output() closeModal = new EventEmitter<void>()
  @Output() createdEmployee = new EventEmitter<Employee>()
  @Output() errorMessage = new EventEmitter<string>()

  form: FormGroup;

  departments: Department[] = [];
  positions: Position[] = [];
  shifts = Object.values(Shift);
  genders = Object.values(Gender);

  inputGroups: Field[][] = [
    [
      { label: 'First Name', name: 'firstName', type: 'input', inputType: 'text', placeholder: 'Mariana' },
      { label: 'Last Name', name: 'lastName', type: 'input', inputType: 'text', placeholder: 'Carvalho' }
    ],
    [
      { label: 'Email', name: 'email', type: 'input', inputType: 'email', placeholder: 'example@email.com' },
      { label: 'Phone', name: 'phone', type: 'input', inputType: 'text', placeholder: '(00) 00000-0000' }
    ],
    [
      { label: 'Gender', name: 'gender', type: 'select', placeholder: 'Select gender', options: this.genders },
      { label: 'Department', name: 'department', type: 'select', placeholder: 'Select department', options: this.departments }
    ],
    [
      { label: 'Position', name: 'position', type: 'select', placeholder: 'Select position', options: this.positions },
      { label: 'Shift', name: 'shift', type: 'select', placeholder: 'Select shift', options: this.shifts }
    ],
    [
      { label: 'Birthday', name: 'birthDate', type: 'input', inputType: 'date', placeholder: '' },
      { label: 'Hired Date', name: 'hiredDate', type: 'input', inputType: 'date', placeholder: '' }
    ]
  ];


  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private positionService: PositionService
  ) {
    this.form = this.fb.group({
      firstName: ['', Validators.required],
      lastName: [''],
      email: ['', [Validators.email]],
      phone: ['', Validators.required],
      gender: [null, Validators.required],
      department: [null, Validators.required],
      position: [null, Validators.required],
      shift: [null],
      birthDate: [null, Validators.required],
      hiredDate: [null, Validators.required],
    });
  }

  ngOnInit() {
    this.getDepartments();
    this.getPositions();
  }

  onClose(): void {
    this.closeModal.emit();
  }

  onSave(): void {

    if(this.form.invalid) {
      this.errorMessage.emit('Fill in all required fields.')
      return
    }

    const newEmployee: CreateEmployee = this.form.value;

    this.employeeService.create(newEmployee).subscribe({
      next: (response: Employee) => {
          this.createdEmployee.emit(response);
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
        this.closeModal.emit()
      }
    })
  }

  getPositions(): void {
    this.positionService.getAll().subscribe({
      next: (response: Position[]) => {
        this.positions = response;
      },
      error: (error) => {
        if(error && error.error) {
          this.errorMessage.emit(error.error);
        } else {
          this.errorMessage.emit('An unexpected error occurred. Please try again later.');
        }
        this.closeModal.emit();
      }
    })
  }

}
