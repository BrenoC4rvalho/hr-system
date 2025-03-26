import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {

  private apiUrl = `${environment.apiUrl}/departments`

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
