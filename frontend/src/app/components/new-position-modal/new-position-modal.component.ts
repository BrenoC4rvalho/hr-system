import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, NgModel, ReactiveFormsModule, Validators } from '@angular/forms';
import { CircleX, LucideAngularModule } from 'lucide-angular';
import { Position } from '../../core/model/position';
import { PositionService } from '../../core/service/position.service';
import { ModalComponent } from "../../shared/modal/modal.component";
import { CreatePosition } from '../../core/model/create-position';
import { Field } from '../../core/types/Field';
import { DynamicFieldComponent } from "../dynamic-field/dynamic-field.component";

@Component({
  selector: 'app-new-position-modal',
  imports: [CommonModule, LucideAngularModule, ModalComponent, ReactiveFormsModule, DynamicFieldComponent],
  templateUrl: './new-position-modal.component.html',
})
export class NewPositionModalComponent {

  @Output() closeModal = new EventEmitter<void>()
  @Output() createdPosition = new EventEmitter<Position>()
  @Output() errorMessage = new EventEmitter<string>()

  form: FormGroup;

  inputGroups: Field[][] = [
    [
      { label: 'Name', name: 'name', type: 'input', inputType: 'text', placeholder: 'Maintenance technician' },
      { label: 'Description', name: 'description', type: 'input', inputType: 'textarea', placeholder: 'specialized maintenance function' }
    ]
  ]

  constructor(
    private fb: FormBuilder,
    private positionService: PositionService
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(255)]]
    })
  }

  onClose(): void {
    this.closeModal.emit();
  }

  onSave(): void {

    if(this.form.invalid) {
      this.errorMessage.emit('Fill in all required fields.')
      return
    }

    const newPosition: CreatePosition = this.form.value;

    this.positionService.create(newPosition).subscribe({
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
