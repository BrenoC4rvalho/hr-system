import { Injectable, NgZone } from '@angular/core';
import { environment } from '../../../environments/environments';
import { Observable } from 'rxjs';
import { AuthService } from '../../auth/service/auth.service';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private apiUrl = `${environment.apiUrl}/chat`

  constructor(
    private authService: AuthService,
    private zone: NgZone,
    private http: HttpClient
  ) { }

  generateResponse(message: string): Observable<string> {
    return new Observable<string>(observer => {
      const controller = new AbortController();
      const token = this.authService.getToken();

      const fetchStream = async () => {
        try {
          const response = await fetch(`${this.apiUrl}?message=${encodeURIComponent(message)}`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${token}`
            },
            signal: controller.signal
          });

          if (!response.body) {
            throw new Error("Response body is null");
          }

          const reader = response.body.getReader();
          const decoder = new TextDecoder();

          while (true) {
            const { done, value } = await reader.read();
            if (done) {
              break;
            }
            const chunk = decoder.decode(value);
            const lines = chunk.split('\n').filter(line => line.trim() !== '');
            for (const line of lines) {
              if (line.startsWith('data:')) {
                const data = line.substring(5).trim();
                this.zone.run(() => {
                  observer.next(data);
                });
              }
            }
          }
          this.zone.run(() => observer.complete());
        } catch (err) {
          if (!controller.signal.aborted) {
            this.zone.run(() => observer.error(err));
          }
        }
      };

      fetchStream();

      return () => {
        controller.abort();
      };
    });
  }

  getFullResponse(sessionId: string): Observable<{ response: string }> {
    return this.http.get<{ response: string }>(`${this.apiUrl}/full/${sessionId}`);
  }

}
