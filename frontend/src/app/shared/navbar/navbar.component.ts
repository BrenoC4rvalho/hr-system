import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutDashboard, LucideAngularModule, Users, ShieldUser, Menu, XCircle, Building, Award } from 'lucide-angular';
import { AuthService } from '../../auth/service/auth.service';
import { User } from '../../core/model/user';
import { CommonModule } from '@angular/common';
import { UserRole } from '../../core/enums/user-role.enum';
import { ErrorModalComponent } from '../../components/error-modal/error-modal';
import { UserProfileModalComponent } from '../../components/user-profile-modal/user-profile-modal.component';

@Component({
  selector: 'app-navbar',
  imports: [RouterModule, LucideAngularModule, CommonModule, ErrorModalComponent, UserProfileModalComponent],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent implements OnInit {

  readonly EmployeeIcon = Users;
  readonly DashboardIcon = LayoutDashboard;
  readonly AdminIcon = ShieldUser;
  readonly MenuIcon = Menu;
  readonly XCircleIcon = XCircle;
  readonly DepartmentIcon = Building;
  readonly PositionIcon = Award;

  isSidebarOpen: boolean = false;

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

  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
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

  showErrorUserProfileModal(errorMessage: string): void {
    this.showUserProfileModal = false;
    this.showErrorModal = true;
    this.errorMessage = errorMessage;
  }

}
