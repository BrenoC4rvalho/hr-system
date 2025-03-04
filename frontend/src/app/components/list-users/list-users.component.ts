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

@Component({
  selector: 'app-list-users',
  imports: [CommonModule, LucideAngularModule, PaginationComponent],
  templateUrl: './list-users.component.html',
})
export class ListUsersComponent implements OnInit, OnChanges {


  @Input() newUser: User | undefined;

  readonly UserRoundSearchIcon = UserRoundSearch;
  readonly EyeIcon = Eye;
  readonly UserXIcon = UserX;

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
        console.error('Error getting users:', error);
      },
      complete: () => {
      }
    })
  }

  onPageChange(newPage: number): void {
    console.log('Page changed')
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

  deleteUser(userId: number): void {
    this.userService.delete(userId).subscribe();
  }

}
