import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CircleX, LucideAngularModule, UserRoundSearch } from 'lucide-angular';
import { UserRole } from '../../core/enums/user-role.enum';
import { CommonModule } from '@angular/common';
import { UserService } from '../../core/service/user.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../../core/model/user';
import { AuthService } from '../../auth/service/auth.service';
import { CreateUser } from '../../core/model/create-user';
import { ModalComponent } from "../../shared/modal/modal.component";
import { Field } from '../../core/types/Field';
import { Employee } from '../../core/model/employee';
import { DynamicFieldComponent } from "../dynamic-field/dynamic-field.component";
import { ListEmployeeSearchComponent } from "../list-employee-search/list-employee-search.component";
import { EmployeeBasic } from '../../core/model/employee-basic';

@Component({
  selector: 'app-new-user-modal',
  imports: [
    LucideAngularModule,
    CommonModule,
    ReactiveFormsModule,
    ModalComponent,
    DynamicFieldComponent,
    ListEmployeeSearchComponent,
    FormsModule
  ],
  templateUrl: './new-user-modal.component.html',
})
export class NewUserModalComponent implements OnInit {

  readonly CircleXIcon = CircleX;
  readonly UserRoundSearchIcon = UserRoundSearch;

  @Output() closeModal = new EventEmitter<void>();
  @Output() createdUser = new EventEmitter<User>();
  @Output() errorMessage = new EventEmitter<string>();

  form: FormGroup;

  userRoles: UserRole[] = [];

  inputGroups: Field[][] = [];

  searchText: string = '';

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService
  ) {
    this.form = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      role: [null, Validators.required],
      employee: [null]
    })
  }

  ngOnInit() {
    this.userRoles = this.getFilterdRoles();

    this.inputGroups = [
      [
        { label: 'Username', name: 'username', type: 'input', inputType: 'text', placeholder: 'employeeHR' },
        { label: 'Role', name: 'role', type: 'select', placeholder: 'Select role', options: this.userRoles }
      ],
    ]
  }


  onClose() {
    this.closeModal.emit();
  }

  private getFilterdRoles(): UserRole[] {

    const roles = Object.values(UserRole);

    if(this.authService.getUser()?.role !== UserRole.ADMIN) {
      return roles.filter(role => role!== UserRole.ADMIN);
    }

    return roles;
  }

  onSave(): void {

    if(this.form.invalid) {
      this.errorMessage.emit('Fill in all required fields.')
      return
    }

    const newUser: CreateUser = this.form.value;

    this.userService.create(newUser).subscribe({
      next: (response: User) => {
        this.createdUser.emit(response);
        this.closeModal.emit();
      },
      error: (error) => {
        if (error && error.error) {
          this.errorMessage.emit(error.error);
        } else {

          this.errorMessage.emit('An unexpected error occurred. Please try again later.');
        }
      }
    })
  }

  onSearch(): string {
    return this.searchText;
  }

  onEmployeeSelected(employee: EmployeeBasic): void {
    this.form.patchValue({ employee });
  }

}
