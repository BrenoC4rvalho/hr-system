import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Position } from '../../core/model/position';
import { ModalComponent } from "../../shared/modal/modal.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-position-profile-modal',
  imports: [ModalComponent, CommonModule],
  templateUrl: './position-profile-modal.component.html',
})
export class PositionProfileModalComponent {

  @Input() position: Position | null = null;
  @Output() closeModal = new EventEmitter<void>();
  @Output() errorMessage = new EventEmitter<string>();

  onClose(): void {
    this.closeModal.emit();
  }

}
