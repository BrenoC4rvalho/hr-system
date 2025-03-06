import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CircleX, LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-confirm-button',
  imports: [LucideAngularModule, CommonModule],
  templateUrl: './confirm-button.component.html',
})
export class ConfirmButtonComponent {

  readonly CircleXIcon = CircleX

  @Input() confirmButtonText: string =  '';
  @Input() isVisible: boolean = false

  @Output() onClosed = new EventEmitter<void>();
  @Output() onConfirm = new EventEmitter<void>();

  onClose() {
    this.onClosed.emit();
    this.isVisible = false;
  }

  confirm() {
    this.onClosed.emit();
    this.isVisible = false;
    this.onConfirm.emit();
  }

}
