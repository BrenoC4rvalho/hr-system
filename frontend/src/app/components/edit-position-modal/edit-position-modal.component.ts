import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Position } from '../../core/model/position';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Field } from '../../core/types/Field';
import { PositionService } from '../../core/service/position.service';
import { ModalComponent } from "../../shared/modal/modal.component";
import { DynamicFieldComponent } from "../dynamic-field/dynamic-field.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-position-modal',
  imports: [ModalComponent, DynamicFieldComponent, CommonModule, ReactiveFormsModule],
  templateUrl: './edit-position-modal.component.html',
})
export class EditPositionModalComponent implements OnInit {

  @Input() position: Position | undefined;
  @Output() closeModal = new EventEmitter<void>();
  @Output() errorMessage = new EventEmitter<string>();
  @Output() editPosition = new EventEmitter<Position>()

  form!: FormGroup;

  inputGroups: Field[][] = [];

  constructor(
    private fb: FormBuilder,
    private positionService: PositionService
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      name: [this.position?.name || '', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      description: [this.position?.description || '', [Validators.required, Validators.minLength(5), Validators.maxLength(255)]]
    });

    this.inputGroups = [
      [
        {
          label: 'Name',
          name: 'name',
          type: 'input',
          inputType: 'text',
          placeholder: 'Mechanic',
          value: this.position?.name || ''
        },
        {
          label: 'Description',
          name: 'description',
          type: 'input',
          inputType: 'text',
          placeholder: 'Mechanic',
          value: this.position?.name || ''
        },
      ]
    ];
  }

  onClose(): void {
    this.closeModal.emit();
  }

  onSave():void {

    if(this.form.invalid) {
          this.errorMessage.emit('Fill in all required fields.')
          return
    }

    const editPosition: Position = this.form.value;

  }
}
