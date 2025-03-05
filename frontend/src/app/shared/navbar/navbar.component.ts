import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutDashboard, LucideAngularModule, Users, ShieldUser } from 'lucide-angular';
import { AuthService } from '../../auth/service/auth.service';
import { User } from '../../core/model/user';
import { CommonModule } from '@angular/common';
import { UserRole } from '../../core/enums/user-role.enum';

@Component({
  selector: 'app-navbar',
  imports: [RouterModule, LucideAngularModule, CommonModule],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent implements OnInit {

  readonly EmployeeIcon = Users;
  readonly DashboardIcon = LayoutDashboard;
  readonly AdminIcon = ShieldUser;

  showUserProfileModal: boolean = false;

  currentUser: User | null = null;

  showErrorModal: boolean = false;
  errorMessage: string = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.user$.subscribe(
      user => {
        this.currentUser = user;
      }
    )
  }

  canAccessAdmin(): boolean {
    return this.currentUser?.role === UserRole.ADMIN || this.currentUser?.role === UserRole.MANAGER;
  }

  logout(): void {
    this.authService.logout();
  }

  openUserProfileModal(): void {
    this.showUserProfileModal = true;
  }

  showErrorUserProfileModal(): void {
    this.showUserProfileModal = false;
    this.showErrorModal = true;
    this.errorMessage = 'An unexpected error occurred while fetching user information. Please try again later.';
  }

}
