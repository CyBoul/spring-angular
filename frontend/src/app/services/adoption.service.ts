import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Adoption {
  id: number;
  petId: number;
  userId: number;
  creationTime: string;
}

@Injectable({ providedIn: 'root' })
export class AdoptionService {

  private readonly url = '/api/adoptions';

  constructor(private http: HttpClient) {}

  adopt(petId: number): Observable<Adoption> {
    return this.http.post<Adoption>(this.url, { petId });
  }

  myAdoptions(): Observable<Adoption[]> {
    return this.http.get<Adoption[]>(`${this.url}/my`);
  }
}
