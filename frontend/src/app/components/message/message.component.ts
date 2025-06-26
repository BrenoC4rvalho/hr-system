import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

export type MessageVariant = 'user' | 'assistant';

@Component({
  selector: 'app-message',
  imports: [CommonModule],
  templateUrl: './message.component.html',
})

export class MessageComponent {

  @Input({ required: true }) text: string = '';
  @Input() variant: MessageVariant = 'assistant';

  public bgColorClass: string = '';
  public textColorClass: string = '';
  public alignmentClass: string = '';

  ngOnInit(): void {
    this.setClassesByVariant();
  }

  private setClassesByVariant(): void {
    if (this.variant === 'user') {
      this.bgColorClass = 'bg-gray-light';
      this.textColorClass = 'text-deep';
      this.alignmentClass = "justify-end"
    } else {
      this.bgColorClass = 'bg-primary';
      this.textColorClass = 'text-white';
      this.alignmentClass = "justify-start"
    }
  }

}
