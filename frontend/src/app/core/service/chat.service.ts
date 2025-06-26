import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private apiUrl = `${environment.apiUrl}/chat`

  constructor(private http: HttpClient) { }

  generateResponse(message: string): Observable<string> {
    return this.http.post(this.apiUrl, message, {
      responseType: 'text'
    });
  }




}
