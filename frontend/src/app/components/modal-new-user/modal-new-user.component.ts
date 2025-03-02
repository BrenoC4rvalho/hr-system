import { Component, EventEmitter, Output } from '@angular/core';
import { AlertCircle, CircleX, LucideAngularModule, XCircle } from 'lucide-angular';
import { UserRole } from '../../core/enums/user-role.enum';
import { CommonModule } from '@angular/common';
import { UserService } from '../../core/service/user.service';
import { FormsModule } from '@angular/forms';
import { User } from '../../core/model/user';
import { ModalErrorComponent } from "../modal-error/modal-error.component";

@Component({
  selector: 'app-modal-new-user',
  imports: [LucideAngularModule, CommonModule, FormsModule, ModalErrorComponent],
  templateUrl: './modal-new-user.component.html',
})
export class ModalNewUserComponent {

  @Output() closeModal = new EventEmitter<void>();

  onClose() {
    this.closeModal.emit();
  }

  readonly CircleXIcon = CircleX

  userRoles = Object.values(UserRole);

  username: string = ''
  role: UserRole = UserRole.HR
  employeeId: number | null = null;

  showErrorModal: boolean = false;
  errorMessage: string = '';

  constructor(private userService: UserService) { }

  onSubmit(): void {
    this.userService.create({ username: this.username, role: this.role, employeeId: this.employeeId }).subscribe({
      next: (response: User) => {
        alert("user created successfully")
      },
      error: (error) => {
        console.log(error)
        if (error && error.error) {
          this.showErrorModal = true;
          this.errorMessage = error.error;
        } else {
          this.showErrorModal = true;
          this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
      },
      complete: () => {
        console.log('User created successfully');
      }
    })
  }




}
