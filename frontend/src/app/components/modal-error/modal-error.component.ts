import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { AlertCircle, LucideAngularModule, XCircle } from 'lucide-angular';

@Component({
  selector: 'app-modal-error',
  imports: [LucideAngularModule, CommonModule],
  templateUrl: './modal-error.component.html',
})
export class ModalErrorComponent {

  readonly XCircleIcon = XCircle;
  readonly AlertCircleIcon = AlertCircle;

  @Input() isVisible: boolean = false;
  @Input() errorMessage: string = '';

  close(): void {
    this.isVisible = false;
  }

}
