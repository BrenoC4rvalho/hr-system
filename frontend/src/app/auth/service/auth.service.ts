import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { AuthResponse } from '../model/auth-response.model';
import { catchError, Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = `${environment.apiUrl}/auth`

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<AuthResponse> {

    return this.http.post<AuthResponse>(this.apiUrl, { username, password}).pipe(
      tap((response) => this.setToken(response.token)),
      catchError((error) => {
        console.error('Login failed:', error);
        throw error;
      }
    ))
  }

  private setToken(token: string): void {
    localStorage.setItem('JWT_Token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('JWT_Token');
  }

  isAuthenticated(): boolean {
    return this.getToken() != null;
  }

  logout() {
    localStorage.removeItem('JWT_Token');
  }

}
