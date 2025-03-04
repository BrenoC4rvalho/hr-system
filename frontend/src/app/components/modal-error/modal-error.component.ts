import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
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
  @Output() closed = new EventEmitter<void>();

  close(): void {
    this.isVisible = false;
    this.closed.emit();
  }

}
