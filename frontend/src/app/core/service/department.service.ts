import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { Observable } from 'rxjs';
import { Department } from '../model/department';
import { CreateDepartment } from '../model/create-department';

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {

  private apiUrl = `${environment.apiUrl}/departments`

  constructor(private http: HttpClient) { }

    getAll(): Observable<Department[]> {
      return this.http.get<Department[]>(this.apiUrl);
    }

    show(id: number): Observable<Department> {
      return this.http.get<Department>(`${this.apiUrl}/${id}`);
    }

    create(body: CreateDepartment): Observable<Department> {
      return this.http.post<Department>(this.apiUrl, body);
    }

    update(id: number, body: Department): Observable<Department> {
      return this.http.put<Department>(`${this.apiUrl}/${id}`, body);
    }

}
