import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Eye, EyeClosed, LucideAngularModule } from 'lucide-angular';
import { Router } from '@angular/router';
import { UserService } from '../../core/service/user.service';
import { ErrorModalComponent } from '../../components/error-modal/error-modal';

@Component({
  selector: 'app-update-password',
  imports: [ LucideAngularModule, CommonModule, FormsModule, ErrorModalComponent ],
  templateUrl: './update-password.component.html',
})
export class UpdatePasswordComponent {

  readonly EyeIcon = Eye;
  readonly EyeClosedIcon = EyeClosed

  showErrorModal = false;
  errorMessage: string = '';

  password: string = '';

  isPasswordVisible: boolean = false;

  constructor(private userService: UserService, private router: Router) {}

  togglePasswordVisibility() {
    this.isPasswordVisible =!this.isPasswordVisible;
  }

  onSubmit(): void {
    this.userService.updatePassword(1, this.password.trim()).subscribe({
      next: (response) => {
        this.router.navigate(['/']);
      },
      error: (error) => {
        if (error && error.error) {
          this.showErrorModal = true;
          this.errorMessage = error.error;
        } else {
          this.showErrorModal = true;
          this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
      }
  })
  }
}
