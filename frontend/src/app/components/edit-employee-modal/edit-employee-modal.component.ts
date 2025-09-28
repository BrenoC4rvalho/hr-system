import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ModalComponent } from "../../shared/modal/modal.component";
import { Employee } from '../../core/model/employee';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Field } from '../../core/types/Field';
import { EmployeeService } from '../../core/service/employee.service';
import { DynamicFieldComponent } from "../dynamic-field/dynamic-field.component";
import { CommonModule } from '@angular/common';
import { DepartmentService } from '../../core/service/department.service';
import { PositionService } from '../../core/service/position.service';
import { Department } from '../../core/model/department';
import { Position } from '../../core/model/position';
import { Shift } from '../../core/enums/shift.enum';
import { Gender } from '../../core/enums/gender.enum';
import { EmployeeStatus } from '../../core/enums/employee-status.enum';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-edit-employee-modal',
  imports: [ModalComponent, DynamicFieldComponent, CommonModule, ReactiveFormsModule],
  templateUrl: './edit-employee-modal.component.html',
})
export class EditEmployeeModalComponent {


  @Input() employee: Employee | undefined;
  @Output() closeModal = new EventEmitter<void>();
  @Output() errorMessage = new EventEmitter<string>();
  @Output()  employeeUpdated = new EventEmitter<Employee>()

  form!: FormGroup;
  inputGroups: Field[][] = [];

  departments: Department[] = [];
  positions: Position[] = [];
  genders = Object.values(Gender);
  shifts = Object.values(Shift);
  statuses = Object.values(EmployeeStatus);

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private positionService: PositionService
  ) {}

  ngOnInit() {
    this.initializeForm();
    this.loadDropdownDataAndSetValues();
  }

  initializeForm(): void {
    this.form = this.fb.group({
      firstName: [this.employee?.firstName || '', Validators.required],
      lastName: [this.employee?.lastName || ''],
      email: [this.employee?.email || '', [Validators.email]],
      phone: [this.employee?.phone || '', Validators.required],
      gender: [this.employee?.gender || null, Validators.required],
      department: [null, Validators.required],
      position: [null, Validators.required],
      shift: [this.employee?.shift || null],
      status: [this.employee?.status || null, Validators.required],
      birthDate: [this.formatDate(this.employee?.birthDate), Validators.required],
      hiredDate: [this.formatDate(this.employee?.hiredDate), Validators.required],
      terminationDate: [this.formatDate(this.employee?.terminationDate)],
    });
  }

  loadDropdownDataAndSetValues(): void {
    const departments$ = this.departmentService.getAll();
    const positions$ = this.positionService.getAll();

    forkJoin([departments$, positions$]).subscribe({
      next: ([departments, positions]) => {
        this.departments = departments;
        this.positions = positions;
        this.updateInputGroups();

        const selectedDepartment = this.departments.find(d => d.id === this.employee?.department.id);
        const selectedPosition = this.positions.find(p => p.id === this.employee?.position.id);

        this.form.patchValue({
          department: selectedDepartment,
          position: selectedPosition
        });
      },
      error: (err) => {
        this.errorMessage.emit('Failed to load required data for editing.');
      }
    });
  }


  updateInputGroups(): void {
    this.inputGroups = [
      [
        { label: 'First Name', name: 'firstName', type: 'input', inputType: 'text' },
        { label: 'Last Name', name: 'lastName', type: 'input', inputType: 'text' }
      ],
      [
        { label: 'Email', name: 'email', type: 'input', inputType: 'email' },
        { label: 'Phone', name: 'phone', type: 'input', inputType: 'text' }
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
        { label: 'Status', name: 'status', type: 'select', placeholder: 'Select status', options: this.statuses },
        { label: 'Birthday', name: 'birthDate', type: 'input', inputType: 'date' }
      ],
      [
        { label: 'Hired Date', name: 'hiredDate', type: 'input', inputType: 'date' },
        { label: 'Termination Date', name: 'terminationDate', type: 'input', inputType: 'date' }
      ]
    ];
  }

  private formatDate(date: Date | string | undefined): string | null {
    if (!date) return null;
    return new Date(date).toISOString().split('T')[0];
  }

  onClose(): void {
    this.closeModal.emit();
  }

  onSave():void {

    if (this.form.invalid) {
      this.errorMessage.emit('Fill in all required fields.');
      return;
    }

    if (this.employee?.id) {
      this.employeeService.update(this.employee.id, this.form.value).subscribe({
        next: (updatedEmployee) => {
          this.employeeUpdated.emit(updatedEmployee);
          this.closeModal.emit();
        },
        error: (err) => {
          this.errorMessage.emit(err.error || 'Failed to update employee.');
        }
      });
    }

  }

}
