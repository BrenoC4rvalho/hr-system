import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChange, SimpleChanges } from '@angular/core';
import { User } from '../../core/model/user';
import { UserService } from '../../core/service/user.service';
import { CircleX, LucideAngularModule } from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserRole } from '../../core/enums/user-role.enum';
import { UserStatus } from '../../core/enums/user-status.enum';

@Component({
  selector: 'app-user-profile-modal',
  imports: [LucideAngularModule, CommonModule, RouterModule, FormsModule],
  templateUrl: './user-profile-modal.component.html',
})
export class UserProfileModalComponent implements OnChanges {

  readonly CircleXIcon = CircleX

  @Input() userId: number | undefined;
  @Input() isVisible: boolean = false;
  @Output() closed = new EventEmitter<void>()
  @Output() errorUserProfileModal = new EventEmitter<void>()

  isEditing = false;

  editableUser: User = {} as User;

  user: User | undefined;

  userRoles: UserRole[] = Object.values(UserRole)
  userStatus: UserStatus[] = Object.values(UserStatus);

  constructor(private userService: UserService) { }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['isVisible'] && this.isVisible) {
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

      },
      error: () => {
        this.errorUserProfileModal.emit();
      }
    })

    return undefined;

  }

  onClose() {
    this.isVisible = false;
    this.closed.emit();
  }

  saveChanges() {

    if (!this.editableUser.username || this.editableUser.username.length < 3 || this.editableUser.username.length > 50) {
      alert('Username must be between 3 and 50 characters.');
      return;
    }

    if (this.userId !== undefined) {
      this.userService.update(this.userId, this.editableUser).subscribe({
        next: (updatedUser) => {
          this.user = updatedUser;
          this.isEditing = false;
        },
        error: (err) => {
          console.error('Error updating user:', err);
          alert('Error updating user.');
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
    }
    this.isEditing = false;
  }

}
