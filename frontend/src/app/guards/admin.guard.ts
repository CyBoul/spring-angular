import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map } from 'rxjs';

export const adminGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.initialized) {
    return auth.isAdmin() || router.createUrlTree(['/pets']);
  }

  return auth.init().pipe(
    map(() => auth.isAdmin() || router.createUrlTree(['/pets']))
  );
};
