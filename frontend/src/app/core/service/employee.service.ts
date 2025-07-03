import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateEmployee } from '../model/create-employee';
import { Employee } from '../model/employee';
import { PaginatedEmployeesResponse } from '../model/paginated-employees-response';
import { BirthdaysResponse } from '../model/birthday-response';
import { EmployeeStatusSummary } from '../model/employee-status-summary';
import { EmployeeBasic } from '../model/employee-basic';
import { EmployeeShiftSummary } from '../model/employee-shift-summary';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private apiUrl = `${environment.apiUrl}/employees`;

  constructor(private http: HttpClient) { }

    getAll(
      page: number,
      size: number,
      positionId?: number | null,
      departmentId?: number | null,
      name?: string
    ): Observable<PaginatedEmployeesResponse> {
      let params = new HttpParams()
       .set('page', page.toString())
       .set('size', size.toString());

       if (positionId) {
        params = params.set('positionId', positionId.toString());
      }

      if (departmentId) {
        params = params.set('departmentId', departmentId.toString());
      }

      if(name) {
        params = params.set('name', name);
      }

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

    getEmployeesByBirthMonth(month: number): Observable<BirthdaysResponse> {
      const params = new HttpParams()
        .set('month', month.toString());

      return this.http.get<BirthdaysResponse>(`${this.apiUrl}/birthdays`, { params });
    }

    getEmployeeStatusSummary(): Observable<EmployeeStatusSummary> {
      return this.http.get<EmployeeStatusSummary>(`${this.apiUrl}/status-summary`);
    }

    getEmployeeShiftSummary(): Observable<EmployeeShiftSummary> {
      return this.http.get<EmployeeShiftSummary>(`${this.apiUrl}/shift-summary`);
    }

    getEmployeeByName(firstName: string, departmentId?: number): Observable<EmployeeBasic[]> {
      let params = new HttpParams();

      if(firstName) {
        params =params.set('firstName', firstName);
      }

      if(departmentId) {
        params = params.set('departmentId', departmentId.toString());
      }

      return this.http.get<EmployeeBasic[]>(`${this.apiUrl}/search`, { params });

    }

    getRecentHires(days: number): Observable<EmployeeBasic[]> {
      const params = new HttpParams().set('days', days.toString());
      return this.http.get<EmployeeBasic[]>(`${this.apiUrl}/recent-hires`, { params });
    }

}
