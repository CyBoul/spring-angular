import { APP_INITIALIZER, ApplicationConfig, ErrorHandler, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { GlobalErrorHandler } from './services/global-error-handler';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';
import { AuthService } from './services/auth.service';
import { authInterceptor } from './filters/jwt.interceptor';
import { routes } from './app.routes';
import { firstValueFrom } from 'rxjs';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(withInterceptors([authInterceptor])),
    providePrimeNG({ theme: { preset: Aura } }),
    { provide: ErrorHandler, useClass: GlobalErrorHandler },
    {
      provide: APP_INITIALIZER,
      useFactory: (auth: AuthService) => () => firstValueFrom(auth.init()),
      deps: [AuthService],
      multi: true,
    },
  ]
};
