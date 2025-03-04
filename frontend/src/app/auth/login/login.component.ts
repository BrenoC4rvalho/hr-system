import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { LucideAngularModule, Eye, EyeClosed, AlertCircle, XCircle } from 'lucide-angular';
import { LogoComponent } from "../../shared/logo/logo.component";
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ModalErrorComponent } from "../../components/error-modal/error-modal";

@Component({
  selector: 'app-login',
  imports: [CommonModule, LucideAngularModule, LogoComponent, FormsModule, ModalErrorComponent],
  templateUrl: './login.component.html',
})
export class LoginComponent {

  isPasswordVisible: boolean = false;

  showErrorModal: boolean = false;
  errorMessage: string = '';

  readonly XCircleIcon = XCircle;
  readonly AlertCircleIcon = AlertCircle;
  readonly EyeIcon = Eye;
  readonly EyeClosedIcon = EyeClosed;

  username: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.authService.login(this.username, this.password).subscribe(
      (response) => {
        this.router.navigate(['/employees']);
      },
      (error) => {
        if(error && error.error) {
          this.showErrorModal = true;
          this.errorMessage = error.error;
        } else {
          this.showErrorModal = true;
          this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
      }
    )
  }

  togglePasswordVisibility() {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

}
