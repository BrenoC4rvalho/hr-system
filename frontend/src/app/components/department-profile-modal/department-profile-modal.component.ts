import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { ModalComponent } from "../../shared/modal/modal.component";
import { Department } from '../../core/model/department';
import { CommonModule } from '@angular/common';
import { DepartmentService } from '../../core/service/department.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-department-profile-modal',
  imports: [ModalComponent, CommonModule, FormsModule],
  templateUrl: './department-profile-modal.component.html',
})
export class DepartmentProfileModalComponent {

  @Input() department: Department | undefined;
  @Output() closeModal = new EventEmitter<void>();
  @Output() errorMessage = new EventEmitter<string>();

  onClose(): void {
    this.closeModal.emit();
  }

}
