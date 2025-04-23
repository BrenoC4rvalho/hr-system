import { Component, EventEmitter, Output } from '@angular/core';
import { CircleX, LucideAngularModule } from 'lucide-angular';
import { UserRole } from '../../core/enums/user-role.enum';
import { CommonModule } from '@angular/common';
import { UserService } from '../../core/service/user.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../../core/model/user';
import { ErrorModalComponent } from "../error-modal/error-modal";
import { AuthService } from '../../auth/service/auth.service';
import { CreateUser } from '../../core/model/create-user';
import { ModalComponent } from "../../shared/modal/modal.component";

@Component({
  selector: 'app-new-user-modal',
  imports: [LucideAngularModule, CommonModule, ReactiveFormsModule, ModalComponent],
  templateUrl: './new-user-modal.component.html',
})
export class NewUserModalComponent {

  readonly CircleXIcon = CircleX

  @Output() closeModal = new EventEmitter<void>();
  @Output() createdUser = new EventEmitter<User>();
  @Output() errorMessage = new EventEmitter<string>();

  form: FormGroup;

  userRoles: UserRole[];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      role: [null, Validators.required],
      employeeId: [null]
    })
    this.userRoles = this.getFilterdRoles();
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

}
