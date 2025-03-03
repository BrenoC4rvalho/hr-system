import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ServerErrorComponent } from "./pages/error/serverError/serverError.component";
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AuthService } from './auth/service/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ServerErrorComponent, CommonModule],
  templateUrl: 'app.component.html'
})
export class AppComponent {

  constructor(private authService: AuthService) {
    this.authService.initializeUser();
  }

  title = 'HR System'

  isServerError: boolean = false;

}
