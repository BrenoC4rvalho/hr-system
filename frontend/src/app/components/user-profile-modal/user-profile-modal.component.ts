import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChange, SimpleChanges } from '@angular/core';
import { User } from '../../core/model/user';
import { UserService } from '../../core/service/user.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserRole } from '../../core/enums/user-role.enum';
import { UserStatus } from '../../core/enums/user-status.enum';
import { AuthService } from '../../auth/service/auth.service';
import { ModalComponent } from "../../shared/modal/modal.component";
import { EmployeeBasic } from '../../core/model/employee-basic';
import { ListEmployeeSearchComponent } from "../list-employee-search/list-employee-search.component";

@Component({
  selector: 'app-user-profile-modal',
  imports: [CommonModule, RouterModule, FormsModule, ModalComponent, ListEmployeeSearchComponent],
  templateUrl: './user-profile-modal.component.html',
})
export class UserProfileModalComponent implements OnChanges {

  @Input() userId: number | undefined;
  @Output() closed = new EventEmitter<void>();
  @Output() errorMessage = new EventEmitter<string>();
  @Output() userUpdated = new EventEmitter<User>();

  isEditing = false;

  editableUser: User = {} as User;

  user: User | undefined;

  userRoles: UserRole[];
  userStatus: UserStatus[] = Object.values(UserStatus);
  selectedEmployeeName: string = '';

  constructor(private userService: UserService, private authService: AuthService) {
    this.userRoles = this.getFilterdRoles();
   }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['userId'] && this.userId) {
      this.getUserProfile();
    }
  }

  getUserProfile(): void {

    if(this.userId === undefined) {
      this.onClose();
      return;
    }

    this.userService.show(this.userId).subscribe({
      next: (response: User) => {
        this.user = response;
        this.editableUser = { ...this.user };
        if (response.employee) {
          this.selectedEmployeeName = `${response.employee.firstName} ${response.employee.lastName}`;
        }
      },
      error: () => {
        this.errorMessage.emit("An unexpected error occurred while fetching user information. Please try again later.");
      }
    })

    return undefined;

  }

  private getFilterdRoles(): UserRole[] {

    const roles = Object.values(UserRole);

    if(this.authService.getUser()?.role !== UserRole.ADMIN) {
      return roles.filter(role => role!== UserRole.ADMIN);
    }

    return roles;
  }

  onClose() {
    this.closed.emit();
  }

  saveChanges() {

    if (!this.editableUser.username || this.editableUser.username.length < 3 || this.editableUser.username.length > 50) {
      this.errorMessage.emit('Username must be between 3 and 50 characters.');
      return;
    }

    if (this.userId !== undefined) {
      this.userService.update(this.userId, this.editableUser).subscribe({
        next: (updatedUser) => {
          this.user = updatedUser;
          this.userUpdated.emit(updatedUser);
          this.isEditing = false;
        },
        error: (err) => {
          if(err.error) {
            this.errorMessage.emit(err.error);
          } else {
            this.errorMessage.emit("An unexpected error occurred while fetching user information. Please try again later.");
          }
        }
      });
    }


  }

  toggleEdit() {
    this.isEditing = true;
  }

  cancelEdit() {
    if(this.user) {
      this.editableUser = {...this.user };
      this.selectedEmployeeName = this.user.employee ? `${this.user.employee.firstName} ${this.user.employee.lastName}` : '';
    }
    this.isEditing = false;
  }

  onEmployeeSelected(employee: EmployeeBasic): void {
    this.editableUser.employeeId = employee.id;
    this.selectedEmployeeName = `${employee.firstName} ${employee.lastName}`;
  }

  canEditAdminOrSelf(): boolean {

    const currentUser = this.authService.getUser();

    if(!currentUser || !this.editableUser) {
      return false;
    }


    if(currentUser.id === this.editableUser.id) {
      return true;
    }

    if(this.editableUser.role === UserRole.ADMIN) {
      return currentUser.role === UserRole.ADMIN;
    }

    return true;

  }

  canEdit(): boolean {

    const currentUser = this.authService.getUser();

    if(currentUser?.role === UserRole.HR) {
      return false;
    }

    return true;

  }

}
