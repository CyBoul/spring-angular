import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pet } from '../models/pet.model';

@Injectable({ providedIn: 'root' })
export class PetService {

  private readonly url = '/api/pets';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Pet[]> {
    return this.http.get<Pet[]>(this.url);
  }

  create(pet: Pet): Observable<Pet> {
    return this.http.post<Pet>(this.url, pet);
  }

  update(id: number, pet: Pet): Observable<Pet> {
    return this.http.put<Pet>(`${this.url}/${id}`, pet);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
