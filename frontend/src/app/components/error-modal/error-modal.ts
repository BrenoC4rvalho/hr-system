import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AlertCircle, LucideAngularModule, XCircle } from 'lucide-angular';

@Component({
  selector: 'app-error-modal',
  imports: [LucideAngularModule, CommonModule],
  templateUrl: './error-modal.component.html',
})
export class ErrorModalComponent {

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
