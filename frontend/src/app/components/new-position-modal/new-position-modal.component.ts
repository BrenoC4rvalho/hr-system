import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule, NgModel } from '@angular/forms';
import { CircleX, LucideAngularModule } from 'lucide-angular';
import { Position } from '../../core/model/position';
import { PositionService } from '../../core/service/position.service';

@Component({
  selector: 'app-new-position-modal',
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './new-position-modal.component.html',
})
export class NewPositionModalComponent {

  readonly CircleXIcon = CircleX;

  name: string = '';
  description: string = '';

  @Output() closeModal = new EventEmitter<void>()
  @Output() createdPosition = new EventEmitter<Position>()
  @Output() errorMessage = new EventEmitter<string>()

  constructor(private positionService: PositionService) {}

  onClose(): void {
    this.closeModal.emit();
  }

  onSubmit(): void {
    this.positionService.create({ name: this.name, description: this.description }).subscribe({
          next: (response: Position) => {
            this.createdPosition.emit(response);
            this.closeModal.emit();
          },
          error: (error) => {
            if(error && error.error) {
              this.errorMessage.emit(error.error);
            } else {
              this.errorMessage.emit('An unexpected error occurred. Please try again later.');
            }
          }
        })
  }


}
