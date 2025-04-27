import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Mail, Phone, LucideAngularModule } from 'lucide-angular';
import { Employee } from '../../core/model/employee';
import { ModalComponent } from "../../shared/modal/modal.component";
import { CommonModule } from '@angular/common';
import { EmployeeStatus } from '../../core/enums/employee-status.enum';

@Component({
  selector: 'app-employee-profile',
  imports: [LucideAngularModule, ModalComponent, CommonModule],
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

  getEmployeeStatusClasses(status: EmployeeStatus): string {
    switch (status) {
      case EmployeeStatus.ACTIVE:
        return 'bg-green-200 text-green-600 border-green-600';
      case EmployeeStatus.TERMINATED:
        return 'bg-red-200 text-red-600 border-red-600';
      case EmployeeStatus.ON_LEAVE:
        return 'bg-yellow-200 text-yellow-600 border-yellow-600';
      case EmployeeStatus.SICK_LEAVE:
        return 'bg-orange-200 text-orange-600 border-orange-600';
      case EmployeeStatus.LEAVE_OF_ABSENCE:
        return 'bg-purple-200 text-purple-600 border-purple-600';
      case EmployeeStatus.RESIGNED:
        return 'bg-gray-200 text-gray-600 border-gray-600';
      default:
        return 'bg-gray-100 text-gray-500 border-gray-400';
    }
  }

}
