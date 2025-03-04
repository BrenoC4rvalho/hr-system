import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PaginatedUsersResponse } from '../model/paginated-users-response';
import { Observable } from 'rxjs';
import { AuthService } from '../../auth/service/auth.service';
import { CreateUser } from '../model/create-user';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient, private authService: AuthService) { }


  getAll(page: number, size: number): Observable<PaginatedUsersResponse> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
    return this.http.get<PaginatedUsersResponse>(this.apiUrl, { params });
  }

  create(body: CreateUser): Observable<User> {
    if (body.username.length < 3 || body.username.length > 50) {
      throw new Error('Username must be between 3 and 50 characters.');
    }
    return this.http.post<User>(this.apiUrl, body);
  }

  //path /id
  // return user 200
  show(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  // path /id  {username: 3,50, role, status, employeeId}
  // return user 200
  update() {

  }


  //path /id  password
  // return string and 200
  updatePassword() {

  }

  // path /id
  // return string and 200
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}
