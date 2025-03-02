import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { PaginatedUsersResponse } from '../model/paginated-users-response';
import { Observable } from 'rxjs';
import { AuthService } from '../../auth/service/auth.service';

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

  // { username: 3,50  role, employeeId: pode ser null }
  // return user 201
  create() {

  }

  //path /id
  // return user 200
  show() {

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
  delete() {

  }

}
