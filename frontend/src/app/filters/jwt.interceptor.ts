import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

const AUTH_URL = `${environment.apiBaseUrl}/auth/`;

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401 && !req.url.includes(AUTH_URL)) {
        router.navigate(['/login']);
      }
      return throwError(() => err);
    })
  );
};
