import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { LucideAngularModule, Eye, EyeClosed } from 'lucide-angular';
import { LogoComponent } from "../../shared/logo/logo.component";

@Component({
  selector: 'app-login',
  imports: [CommonModule, LucideAngularModule, LogoComponent],
  templateUrl: './login.component.html',
})
export class LoginComponent {

  isPasswordVisible: boolean = false;

  readonly EyeIcon = Eye;
  readonly EyeClosedIcon = EyeClosed;

  togglePasswordVisibility() {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

}
