import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './auth/service/auth.service';
import { HttpClient } from '@angular/common/http';
import { ChatComponent } from "./components/chat/chat.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, ChatComponent],
  templateUrl: 'app.component.html'
})
export class AppComponent {

  constructor(private router: Router, private http: HttpClient, private authService: AuthService) {
    this.authService.initializeUser();
    //this.serverRunning();
  }

  title = 'HR System'

  serverRunning() {
    this.http.get('http://localhost:8080/auth').subscribe({
      next:() => {

      },
      error: (err) => {
        console.error('Error: ', err)
        if (
          err.status === 0
        ) {
          this.router.navigate(['/serverError']);
        }
      }
    })
  }

}
