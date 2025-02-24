import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ServerErrorComponent } from "./pages/error/serverError/serverError.component";
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ServerErrorComponent, CommonModule],
  templateUrl: 'app.component.html'
})
export class AppComponent {
  title = 'HR System'

  isServerError: boolean = false;

}
