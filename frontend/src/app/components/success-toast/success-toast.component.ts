import { animate, style, transition, trigger } from '@angular/animations';
import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { CheckCircle, LucideAngularModule, XCircle } from 'lucide-angular';

@Component({
  selector: 'app-success-toast',
  imports: [LucideAngularModule, CommonModule],
  templateUrl: './success-toast.component.html',
  animations: [
    trigger('fadeAnimation', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(20px)' }),
        animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' })),
      ]),
      transition(':leave', [
        animate('400ms ease-in', style({ opacity: 0, transform: 'translateY(20px)' })),
      ]),
    ]),
  ],
})
export class SuccessToastComponent {

  readonly CheckCircleIcon = CheckCircle;
  readonly XCircleIcon = XCircle;

  @Input() isVisible: boolean = false;
  @Input() successMessage: string = '';
  @Output() closed = new EventEmitter<void>();

  autoCloseDelay = 5000; // 5s
  timeoutRef: any;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['isVisible'] && this.isVisible === true) {
      clearTimeout(this.timeoutRef);
      this.closeModalAfterDelay();
    }
  }

  close(): void {
    this.isVisible = false;
    this.closed.emit();
  }

  private closeModalAfterDelay(): void {
    this.timeoutRef = setTimeout(() => {
      this.close();
    }, this.autoCloseDelay);
  }

}
