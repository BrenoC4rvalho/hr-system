import { Component } from '@angular/core';
import { Mail, Phone, LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-employee-profile',
  imports: [LucideAngularModule],
  templateUrl: './employee-profile.component.html',
})
export class EmployeeProfileComponent {

  readonly MailIcon = Mail;
  readonly PhoneIcon = Phone;

}
