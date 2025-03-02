import { Component, OnInit } from '@angular/core';
import { User } from '../../core/model/user';
import { UserService } from '../../core/service/user.service';
import { PaginatedUsersResponse } from '../../core/model/paginated-users-response';
import { CommonModule } from '@angular/common';
import { Eye, LucideAngularModule, UserRoundSearch, UserX } from 'lucide-angular';

@Component({
  selector: 'app-list-users',
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './list-users.component.html',
})
export class ListUsersComponent implements OnInit {

  readonly UserRoundSearchIcon = UserRoundSearch;
  readonly EyeIcon = Eye;
  readonly UserXIcon = UserX;

  users: User[] = [];

  currentPage: number = 0;
  pageSize: number = 12;
  totalPages: number = 0;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.getUsers(this.currentPage, this.pageSize)
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
        console.log('Users retrieved successfully');
        console.log(this.users)
      }
    })
  }

}
