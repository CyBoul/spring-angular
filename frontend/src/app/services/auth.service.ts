import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, map, tap, catchError, of } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly authUrl = `${environment.apiBaseUrl}/auth`;
  private role$ = new BehaviorSubject<string | null>(null);
  private _initialized = false;

  constructor(private http: HttpClient) {}

  /**
   * Called once via APP_INITIALIZER to restore session from httpOnly cookie.
   * If the cookie is valid the server returns the role; otherwise we stay logged out.
   */
  init(): Observable<boolean> {
    return this.http.get<{ role: string }>(`${this.authUrl}/me`).pipe(
      tap(res => this.role$.next(res.role)),
      map(() => true),
      catchError(() => {
        this.role$.next(null);
        return of(false);
      }),
      tap(() => this._initialized = true),
    );
  }

  get initialized(): boolean {
    return this._initialized;
  }

  login(email: string, password: string): Observable<void> {
    return this.http
      .post<{ role: string }>(`${this.authUrl}/login`, { email, password })
      .pipe(
        tap(res => this.role$.next(res.role)),
        map(() => void 0),
      );
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.authUrl}/logout`, {}).pipe(
      tap(() => this.role$.next(null)),
      map(() => void 0),
      catchError(() => {
        this.role$.next(null);
        return of(void 0);
      }),
    );
  }

  isLoggedIn(): boolean {
    return this.role$.value !== null;
  }

  isAdmin(): boolean {
    return this.role$.value === 'ROLE_ADMIN';
  }

  loggedIn$(): Observable<boolean> {
    return this.role$.pipe(map(r => r !== null));
  }
}
