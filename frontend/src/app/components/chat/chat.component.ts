import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CircleX, LucideAngularModule, SendHorizontal } from 'lucide-angular';
import { MessageComponent } from "../message/message.component";
import { ChatService } from '../../core/service/chat.service';
import { FormsModule } from '@angular/forms';
import { ChatMessage } from '../../core/types/ChatMessage';
import { ErrorModalComponent } from "../error-modal/error-modal";
import { animate, keyframes, query, stagger, style, transition, trigger } from '@angular/animations';
import { TextChunkProcessor } from '../../core/utils/textChunkProcessor';

@Component({
  selector: 'app-chat',
  imports: [CommonModule, FormsModule, LucideAngularModule, MessageComponent, ErrorModalComponent],
  templateUrl: './chat.component.html',

})
export class ChatComponent {

  readonly CircleXIcon = CircleX;
  readonly SendIcon = SendHorizontal;

  isVisible: boolean = false;
  userMessage: string = '';
  isLoading: boolean = false;

  showErrorModal: boolean = false;
  errorMessage: string = '';

  public conversation: ChatMessage[] = [];
  private textProcessor = new TextChunkProcessor();
  private currentSessionId: string | null = null;

  constructor(private chatService: ChatService) {}

  openChat(): void {
    this.isVisible = true;
  }

  closeChat(): void {
    this.isVisible = false;
  }

  handleErrorModal(message: string) {
    this.showErrorModal = true;
    this.errorMessage = message;
  }

  sendMessage(): void {
    if(!this.userMessage.trim()) {
      return
    }

    const messageToSend = this.userMessage;
    this.conversation.push({ text: messageToSend, variant: 'user' });
    this.userMessage = '';
    this.isLoading = true;
    this.textProcessor.reset();
    this.currentSessionId = null;

    this.conversation.push({ text: '', variant: 'assistant' });

    this.chatService.generateResponse(messageToSend).subscribe({
      next: (chunk) => {
        if (chunk.startsWith('sessionId:')) {
          this.currentSessionId = chunk.substring(10);
          return;
        }
        const lastMessage = this.conversation[this.conversation.length - 1];
        if(lastMessage) {
            lastMessage.text += this.textProcessor.process(chunk);
        }
      },
      error: (err) => {
        console.error('Chat API Error:', err);
        if(err && err.error) {
          this.handleErrorModal(err.error);
        } else {
          this.handleErrorModal('An unexpected error occurred. Please try again later.');
        }
        this.conversation.pop();
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
        if (this.currentSessionId) {
          this.fetchFullResponse(this.currentSessionId);
        }
      }
    })
  }

  fetchFullResponse(sessionId: string): void {
    this.chatService.getFullResponse(sessionId).subscribe({
      next: (response) => {
        const lastMessage = this.conversation[this.conversation.length - 1];
        if (lastMessage && lastMessage.variant === 'assistant') {
          lastMessage.text = response.response;
        }
      },
      error: (err) => {
        console.error('Error fetching full response:', err);
      }
    });
  }

}
