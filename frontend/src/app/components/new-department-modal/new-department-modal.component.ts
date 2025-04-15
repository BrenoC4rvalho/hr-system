import { Component, EventEmitter, Output } from '@angular/core';
import { CircleX, LucideAngularModule } from 'lucide-angular';
import { DepartmentService } from '../../core/service/department.service';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-new-department-modal',
  imports: [LucideAngularModule, FormsModule, CommonModule],
  templateUrl: './new-department-modal.component.html',
})
export class NewDepartmentModalComponent {

  readonly CircleXIcon = CircleX;

  @Output() closeModal = new EventEmitter<void>()

  name: string = ''

  constructor(private departmentService: DepartmentService) {}

  onSubmit(): void {

  }

  onClose() {
    this.closeModal.emit();
  }

}
