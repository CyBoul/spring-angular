import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './layout/navbar/navbar.component';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent],
  template: `
    @if (auth.isLoggedIn()) {
      <app-navbar />
    }
    <router-outlet />
  `
})
export class AppComponent {
  constructor(public auth: AuthService) {}
}
