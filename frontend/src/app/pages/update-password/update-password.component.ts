import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Eye, EyeClosed, LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-update-password',
  imports: [ LucideAngularModule, CommonModule, FormsModule ],
  templateUrl: './update-password.component.html',
})
export class UpdatePasswordComponent {

  readonly EyeIcon = Eye;
  readonly EyeClosedIcon = EyeClosed

  password: string = '';

  isPasswordVisible: boolean = false;

  togglePasswordVisibility() {
    this.isPasswordVisible =!this.isPasswordVisible;
  }
}
