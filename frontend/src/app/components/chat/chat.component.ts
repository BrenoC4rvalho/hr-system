import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CircleX, LucideAngularModule, SendHorizontal } from 'lucide-angular';
import { MessageComponent } from "../message/message.component";

@Component({
  selector: 'app-chat',
  imports: [CommonModule, LucideAngularModule, MessageComponent],
  templateUrl: './chat.component.html',
})
export class ChatComponent {

  readonly CircleXIcon = CircleX;
  readonly SendIcon = SendHorizontal;

  isVisible: boolean = false;

  openChat(): void {
    this.isVisible = true;
  }

  closeChat(): void {
    this.isVisible = false;
  }

}
