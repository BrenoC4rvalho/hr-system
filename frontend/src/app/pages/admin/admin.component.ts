import { Component } from '@angular/core';
import { NavbarComponent } from '../../shared/navbar/navbar.component';
import { ListUsersComponent } from '../../components/list-users/list-users.component';
import { UserPlus, LucideAngularModule } from 'lucide-angular';
import { NewUserModalComponent } from "../../components/new-user-modal/new-user-modal.component";
import { CommonModule } from '@angular/common';
import { User } from '../../core/model/user';
import { ErrorModalComponent } from "../../components/error-modal/error-modal";

@Component({
  selector: 'app-admin',
  imports: [NavbarComponent, ListUsersComponent, LucideAngularModule, NewUserModalComponent, CommonModule, ErrorModalComponent],
  templateUrl: './admin.component.html',
})
export class AdminComponent {

  showErrorModal: boolean = false;
  errorMessage: string = ''

  newUser: User | undefined;

  readonly UserPlusIcon = UserPlus;

  isModalNewUserOpen: boolean = false;

  openModalNewUser() {
    this.isModalNewUserOpen = true;
  }

  closeModalNewUser() {
    this.isModalNewUserOpen = false;
  }

  onUserCreated($event: User) {
    this.newUser = $event;
  }

  handleErrorModal($event: string) {
    this.showErrorModal = true;
    this.errorMessage = $event;
  }

}
