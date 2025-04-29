import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ModalComponent } from "../../shared/modal/modal.component";
import { Department } from '../../core/model/department';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-department-profile-modal',
  imports: [ModalComponent, CommonModule],
  templateUrl: './department-profile-modal.component.html',
})
export class DepartmentProfileModalComponent {

  @Input() department: Department | null = null;
  @Output() closeModal = new EventEmitter<void>();
  @Output() errorMessage = new EventEmitter<string>();

  onClose(): void {
    this.closeModal.emit();
  }

}
