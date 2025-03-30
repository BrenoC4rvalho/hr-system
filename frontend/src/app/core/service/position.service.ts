import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { Observable } from 'rxjs';
import { CreatePosition } from '../model/create-position';
import { Position } from '../model/position';

@Injectable({
  providedIn: 'root'
})
export class PositionService {

  private apiUrl = `${environment.apiUrl}/positions`;

  constructor(private http: HttpClient) { }

  getAll(page: number, size: number): Observable<Position[]> {
    const params = new HttpParams()
     .set('page', page.toString())
     .set('size', size.toString());
    return this.http.get<Position[]>(this.apiUrl, { params });
  }

  show(id: number): Observable<Position> {
    return this.http.get<Position>(`${this.apiUrl}/${id}`);
  }

  create(body: CreatePosition): Observable<Position> {
    return this.http.post<Position>(this.apiUrl, body);
  }

  update(id: number, body: Position): Observable<Position> {
    return this.http.put<Position>(`${this.apiUrl}/${id}`, body);
  }

}
