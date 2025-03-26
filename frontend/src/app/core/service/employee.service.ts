import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private apiUrl = `${environment.apiUrl}/employees`;

  constructor(private http: HttpClient) { }

    getAll(page: number, size: number): Observable<any[]> {
      const params = new HttpParams()
       .set('page', page.toString())
       .set('size', size.toString());
      return this.http.get<any[]>(this.apiUrl, { params });
    }

    show(id: number): Observable<any> {
      return this.http.get<any>(`${this.apiUrl}/${id}`);
    }
}
