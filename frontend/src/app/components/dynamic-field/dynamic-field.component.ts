import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Field } from '../../core/types/Field';

@Component({
  selector: 'app-dynamic-field',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './dynamic-field.component.html',
})
export class DynamicFieldComponent {

  @Input() field: any;
  @Input() form!: FormGroup;

  get control() {
    return this.form.get(this.field.name);
  }

}

