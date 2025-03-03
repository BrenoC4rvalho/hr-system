import { Component, EventEmitter, Output } from '@angular/core';
import { CircleX, LucideAngularModule } from 'lucide-angular';
import { UserRole } from '../../core/enums/user-role.enum';
import { CommonModule } from '@angular/common';
import { UserService } from '../../core/service/user.service';
import { FormsModule } from '@angular/forms';
import { User } from '../../core/model/user';
import { ModalErrorComponent } from "../modal-error/modal-error.component";
import { AuthService } from '../../auth/service/auth.service';

@Component({
  selector: 'app-modal-new-user',
  imports: [LucideAngularModule, CommonModule, FormsModule, ModalErrorComponent],
  templateUrl: './modal-new-user.component.html',
})
export class ModalNewUserComponent {

  readonly CircleXIcon = CircleX

  @Output() closeModal = new EventEmitter<void>();
  @Output() createdUser = new EventEmitter<User>();


  username: string = ''
  role: UserRole = UserRole.HR
  employeeId: number | null = null;

  showErrorModal: boolean = false;
  errorMessage: string = '';

  userRoles: UserRole[];

  constructor(private userService: UserService, private authService: AuthService) {
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

  onSubmit(): void {
    this.userService.create({ username: this.username, role: this.role, employeeId: this.employeeId }).subscribe({
      next: (response: User) => {
        this.createdUser.emit(response);
        this.closeModal.emit();
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
      }
    })
  }

}
