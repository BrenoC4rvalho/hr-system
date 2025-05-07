import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CircleX, LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-modal',
  imports: [LucideAngularModule],
  templateUrl: './modal.component.html',
})
export class ModalComponent {

  readonly CircleXIcon = CircleX;

  @Input() headerText: string = '';
  @Output() closeCliked = new EventEmitter<void>();

  close(): void {
    this.closeCliked.emit();
  }


}
