import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly TOKEN_KEY = 'jwt_token';
  private readonly ROLE_KEY = 'jwt_role';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<{ token: string; role: string }> {
    return this.http
      .post<{ token: string; role: string }>('/api/auth/login', { email, password })
      .pipe(
        tap(({ token, role }) => {
          localStorage.setItem(this.TOKEN_KEY, token);
          localStorage.setItem(this.ROLE_KEY, role);
        })
      );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.ROLE_KEY);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    return localStorage.getItem(this.ROLE_KEY) === 'ROLE_ADMIN';
  }
}
