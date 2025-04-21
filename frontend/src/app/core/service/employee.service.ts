import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateEmployee } from '../model/create-employee';
import { Employee } from '../model/employee';
import { PaginatedEmployeesResponse } from '../model/paginated-employees-response';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private apiUrl = `${environment.apiUrl}/employees`;

  constructor(private http: HttpClient) { }

    getAll(page: number, size: number): Observable<PaginatedEmployeesResponse> {
      const params = new HttpParams()
       .set('page', page.toString())
       .set('size', size.toString());
      return this.http.get<PaginatedEmployeesResponse>(this.apiUrl, { params });
    }

    show(id: number): Observable<Employee> {
      return this.http.get<Employee>(`${this.apiUrl}/${id}`);
    }

    create(body: CreateEmployee): Observable<Employee> {
      return this.http.post<Employee>(this.apiUrl, body);
    }

    update(id: number, body: Employee): Observable<Employee> {
      return this.http.put<Employee>(`${this.apiUrl}/${id}`, body);
    }
}
