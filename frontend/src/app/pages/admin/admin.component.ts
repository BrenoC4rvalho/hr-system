import { Component } from '@angular/core';
import { NavbarComponent } from '../../shared/navbar/navbar.component';
import { ListUsersComponent } from '../../components/list-users/list-users.component';
import { UserPlus, LucideAngularModule } from 'lucide-angular';
import { ModalNewUserComponent } from "../../components/modal-new-user/modal-new-user.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin',
  imports: [NavbarComponent, ListUsersComponent, LucideAngularModule, ModalNewUserComponent, CommonModule],
  templateUrl: './admin.component.html',
})
export class AdminComponent {

  readonly UserPlusIcon = UserPlus;

  isModalNewUserOpen: boolean = false;

  openModalNewUser() {
    this.isModalNewUserOpen = true;
  }

  closeModalNewUser() {
    this.isModalNewUserOpen = false;
  }


}
