import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {

  private apiUrl = `${environment.apiUrl}/departments`

  constructor(private http: HttpClient) { }

}
