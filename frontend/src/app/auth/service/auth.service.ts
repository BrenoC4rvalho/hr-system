import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { AuthResponse } from '../model/auth-response.model';
import { BehaviorSubject, catchError, Observable, tap } from 'rxjs';
import { User } from '../../core/model/user';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = `${environment.apiUrl}`

  private userSubject = new BehaviorSubject<User | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  initializeUser(): void {
    if(this.getToken()) {
      this.loadUser().subscribe();
    }
  }

  login(username: string, password: string): Observable<AuthResponse> {

    return this.http.post<AuthResponse>(`${this.apiUrl}/auth`, { username, password}).pipe(
      tap((response) => {
        this.setToken(response.token);
        this.initializeUser();
      }),
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

  loadUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/me`).pipe(
      tap((user) => {
        this.userSubject.next(user);
      }),
      catchError(error => {
        this.userSubject.next(null);
        this.logout();
        throw error;
      })
    )
  }

  getUser(): User | null {
    return this.userSubject.value;
  }

  isAuthenticated(): boolean {
    return this.getToken() != null;
  }

  logout() {
    localStorage.removeItem('JWT_Token');
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

}
