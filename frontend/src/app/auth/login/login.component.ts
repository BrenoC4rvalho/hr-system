import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { LucideAngularModule, Eye, EyeClosed, AlertCircle, XCircle } from 'lucide-angular';
import { LogoComponent } from "../../shared/logo/logo.component";
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [CommonModule, LucideAngularModule, LogoComponent, FormsModule],
  templateUrl: './login.component.html',
})
export class LoginComponent {

  isPasswordVisible: boolean = false;
  loginError: boolean = false;

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
        this.loginError = true
      }
    )
  }

  togglePasswordVisibility() {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

}
