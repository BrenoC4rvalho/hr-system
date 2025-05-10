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

  @Input() position: Position | null = null;
  @Output() closeModal = new EventEmitter<void>();
  @Output() errorMessage = new EventEmitter<string>();

  isEditing = false;
  editablePosition: Position = {} as Position;

  constructor(private positionService: PositionService) {}

  onClose(): void {
    this.closeModal.emit();
  }

  toggleEdit(): void {
    if (this.position) {
      this.editablePosition = { ...this.position };
      this.isEditing = true;
    }
  }

  cancelEdit(): void {
    this.isEditing = false;
  }

  saveChanges(): void {
    if (!this.editablePosition.name || this.editablePosition.name.length < 3) {
      this.errorMessage.emit('Position name must be at least 3 characters long.');
      return;
    }

    if (this.position?.id) {
      this.positionService.update(this.position.id, this.editablePosition).subscribe({
        next: (updatedPosition: Position) => {
          this.position = updatedPosition;
          this.isEditing = false;
        },
        error: (err) => {
          this.errorMessage.emit(err?.error || 'Failed to update position.');
        }
      });
    }
  }
}
