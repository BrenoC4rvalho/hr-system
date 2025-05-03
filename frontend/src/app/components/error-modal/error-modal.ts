import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { AlertCircle, LucideAngularModule, XCircle } from 'lucide-angular';
import { trigger, transition, style, animate } from '@angular/animations';

@Component({
  selector: 'app-error-modal',
  imports: [LucideAngularModule, CommonModule],
  templateUrl: './error-modal.component.html',
  animations: [
    trigger('fadeAnimation', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('200ms ease-in', style({ opacity: 1 })),
      ]),
      transition(':leave', [
        animate('400ms ease-out', style({ opacity: 0 })),
      ]),
    ]),
  ],
})
export class ErrorModalComponent implements OnChanges {

  readonly XCircleIcon = XCircle;
  readonly AlertCircleIcon = AlertCircle;

  @Input() isVisible: boolean = false;
  @Input() errorMessage: string = '';
  @Output() closed = new EventEmitter<void>();

  autoCloseDelay = 10000;
  timeoutRef: any;
  visibleState: string = 'visible';

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['isVisible'] && this.isVisible === true) {
      clearTimeout(this.timeoutRef);
      this.visibleState = 'visible';
      this.closeModalAfterDelay(this.autoCloseDelay);
    }
  }

  close(): void {
    this.isVisible = false;
    this.closed.emit();
  }

  closeModalAfterDelay(delay: number = 10000): void {
    this.timeoutRef = setTimeout(() => {
      this.close();
    }, delay);
  }

}
