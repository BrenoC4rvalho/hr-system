import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CircleX, LucideAngularModule, SendHorizontal } from 'lucide-angular';
import { MessageComponent } from "../message/message.component";
import { ChatService } from '../../core/service/chat.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-chat',
  imports: [CommonModule, FormsModule, LucideAngularModule, MessageComponent],
  templateUrl: './chat.component.html',
})
export class ChatComponent {

  readonly CircleXIcon = CircleX;
  readonly SendIcon = SendHorizontal;

  isVisible: boolean = false;
  userMessage: string = '';
  fullResponse: string = '';
  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(private chatService: ChatService) {}

  openChat(): void {
    this.isVisible = true;
  }

  closeChat(): void {
    this.isVisible = false;
  }

  sendMessage(): void {
    if(!this.userMessage.trim()) {
      return
    }

    this.isLoading = true;
    this.fullResponse = '';
    this.errorMessage = '';
    const messageToSend = this.userMessage;
    this.userMessage = '';

    this.chatService.generateResponse(messageToSend).subscribe({
      next: (chunk) => {
        this.fullResponse += chunk;
      },
      error: (err) => {
        console.error('Chat API Error:', err);
        this.errorMessage = 'An error occurred. Please try again.';
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    })
  }

}
