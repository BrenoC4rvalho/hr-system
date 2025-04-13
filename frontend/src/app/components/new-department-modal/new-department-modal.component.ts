import { Component, EventEmitter, Output } from '@angular/core';
import { CircleX, LucideAngularModule } from 'lucide-angular';
import { DepartmentService } from '../../core/service/department.service';

@Component({
  selector: 'app-new-department-modal',
  imports: [LucideAngularModule],
  templateUrl: './new-department-modal.component.html',
})
export class NewDepartmentModalComponent {

  readonly CircleXIcon = CircleX;

  @Output() closeModal = new EventEmitter<void>()

  constructor(private departmentService: DepartmentService) {}

  onClose() {
    this.closeModal.emit();
  }

}
