import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChange, SimpleChanges } from '@angular/core';
import { User } from '../../core/model/user';
import { UserService } from '../../core/service/user.service';
import { CircleX, LucideAngularModule } from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-user-profile-modal',
  imports: [LucideAngularModule, CommonModule, RouterModule],
  templateUrl: './user-profile-modal.component.html',
})
export class UserProfileModalComponent implements OnChanges {

  readonly CircleXIcon = CircleX

  @Input() userId: number | undefined;
  @Input() isVisible: boolean = false;
  @Output() closed = new EventEmitter<void>()
  @Output() errorUserProfileModal = new EventEmitter<void>()


  user: User | undefined;

  constructor(private userService: UserService) { }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['isVisible'] && this.isVisible) {
      this.getUserProfile()
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

}
