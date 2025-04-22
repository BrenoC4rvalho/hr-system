import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  imports: [],
  templateUrl: './modal.component.html',
})
export class ModalComponent {

  @Input() headerText: string = '';
  @Output() saveClicked = new EventEmitter<void>();
  @Output() closeCliked = new EventEmitter<void>();

  save(): void {
    this.saveClicked.emit();
  }

  close(): void {
    this.closeCliked.emit();
  }


}
