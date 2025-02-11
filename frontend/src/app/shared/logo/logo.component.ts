import { Component } from '@angular/core';
import { LucideAngularModule, Box } from 'lucide-angular';

@Component({
  selector: 'app-logo',
  imports: [LucideAngularModule],
  templateUrl: './logo.component.html',
})
export class LogoComponent {
  readonly CubeIcon = Box;
}
