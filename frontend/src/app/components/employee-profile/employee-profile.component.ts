import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Mail, Phone, LucideAngularModule } from 'lucide-angular';
import { Employee } from '../../core/model/employee';
import { ModalComponent } from "../../shared/modal/modal.component";

@Component({
  selector: 'app-employee-profile',
  imports: [LucideAngularModule, ModalComponent],
  templateUrl: './employee-profile.component.html',
})
export class EmployeeProfileComponent {

  readonly MailIcon = Mail;
  readonly PhoneIcon = Phone;

  @Input() employee: Employee | null = null;
  @Output() closeModal = new EventEmitter<void>();
  @Output() errorMessage = new EventEmitter<string>();

  onClose() {
    this.closeModal.emit();
  }
}
