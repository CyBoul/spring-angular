import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map } from 'rxjs';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.initialized) {
    return auth.isLoggedIn() || router.createUrlTree(['/login']);
  }

  return auth.init().pipe(
    map(loggedIn => loggedIn || router.createUrlTree(['/login']))
  );
};
