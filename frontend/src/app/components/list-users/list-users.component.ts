import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { User } from '../../core/model/user';
import { UserService } from '../../core/service/user.service';
import { PaginatedUsersResponse } from '../../core/model/paginated-users-response';
import { CommonModule } from '@angular/common';
import { Eye, LucideAngularModule, UserRoundSearch, UserX } from 'lucide-angular';
import { AuthService } from '../../auth/service/auth.service';
import { UserRole } from '../../core/enums/user-role.enum';
import { UserStatus } from '../../core/enums/user-status.enum';
import { PaginationComponent } from "../pagination/pagination.component";
import { ErrorModalComponent } from "../error-modal/error-modal";
import { UserProfileModalComponent } from "../user-profile-modal/user-profile-modal.component";
import { ConfirmButtonComponent } from "../confirm-button/confirm-button.component";

@Component({
  selector: 'app-list-users',
  imports: [CommonModule, LucideAngularModule, PaginationComponent, ErrorModalComponent, UserProfileModalComponent, ConfirmButtonComponent],
  templateUrl: './list-users.component.html',
})
export class ListUsersComponent implements OnInit, OnChanges {


  @Input() newUser: User | undefined;

  readonly UserRoundSearchIcon = UserRoundSearch;
  readonly EyeIcon = Eye;
  readonly UserXIcon = UserX;

  showErrorModal: boolean = false;
  errorMessage: string = '';

  showConfirmButton: boolean = false;
  currentUserIdToDelete: number | undefined;
  currentUsernameToDelete: string | undefined;

  showUserProfileModal: boolean = false;
  currentUserId: number | undefined;

  users: User[] = [];

  currentPage: number = 0;
  pageSize: number = 12;
  totalPages: number = 0;

  constructor(private userService: UserService, private authService: AuthService) {}

  ngOnInit(): void {
    this.getUsers(this.currentPage, this.pageSize)
  }

  ngOnChanges(changes: SimpleChanges): void {
      if(this.newUser !== undefined) {
        this.users.push(this.newUser);
        this.newUser = undefined;
      }
  }

  getUsers(page: number, size: number): void {
    this.userService.getAll(page, size).subscribe({
      next: (response: PaginatedUsersResponse) => {
        this.users = response.users;
        this.currentPage = response.currentPage;
        this.totalPages = response.totalPages;
        this.pageSize = response.pageSize;
      },
      error: (error) => {
        if(error && error.error) {
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

  onPageChange(newPage: number): void {
    this.getUsers(newPage, this.pageSize);
  }

  canDeleteAdmin(userDeleteRole: UserRole): boolean {
    if(!(this.authService.getUser()?.role === UserRole.ADMIN) && userDeleteRole === UserRole.ADMIN) {
      return false;
    }

    return true;
  }

  canUserActive(status: UserStatus): boolean {
    return status === UserStatus.ACTIVE;
  }

  showConfirmDeleteUser(userId: number, username: string) {
    this.currentUserIdToDelete = userId;
    this.currentUsernameToDelete = username;
    this.showConfirmButton = true;
  }

  deleteUser(userId: number): void {
    this.userService.delete(userId).subscribe({
      next: () => {
        const user = this.users.find(user => user.id === userId);
        user ? user.status = UserStatus.INACTIVE : '';
      },
      error: (error) => {
        if(error && error.error) {
          this.showErrorModal = true;
          this.errorMessage = error.error;
        } else {
          this.showErrorModal = true;
          this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
      },
      complete: () => {
        this.showConfirmButton = false;
      }
    });
  }

  openUserProfileModal(userId: number): void {
    this.currentUserId = userId;
    this.showUserProfileModal = true;
  }

  showErrorUserProfileModal(errorMessage: string): void {
    this.showUserProfileModal = false;
    this.showErrorModal = true;
    this.errorMessage = errorMessage;
  }

}
