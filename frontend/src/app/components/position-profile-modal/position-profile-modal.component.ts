import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalComponent } from '../../shared/modal/modal.component';
import { Position } from '../../core/model/position';
import { FormsModule } from '@angular/forms';
import { PositionService } from '../../core/service/position.service';

@Component({
  selector: 'app-position-profile-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalComponent],
  templateUrl: './position-profile-modal.component.html',
})
export class PositionProfileModalComponent {

  @Input() position: Position | undefined;
  @Output() closeModal = new EventEmitter<void>();

  onClose(): void {
    this.closeModal.emit();
  }

}
