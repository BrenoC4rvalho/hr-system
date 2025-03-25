import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class PositionService {

  private apiUrl = `${environment.apiUrl}/positions`;

  constructor(private http: HttpClient) { }
}
