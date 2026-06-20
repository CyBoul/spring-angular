import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, ButtonModule],
  template: `
    <nav class="navbar">
      <span class="brand" routerLink="/pets">🐾 PetAdopt</span>
      <div class="nav-links">
        <a routerLink="/pets">Pets</a>
        @if (auth.isAdmin()) {
          <a routerLink="/admin/pets">Admin</a>
        }
        <p-button label="Logout" severity="secondary" size="small" (onClick)="logout()" />
      </div>
    </nav>
  `,
  styles: [`
    .navbar {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0.75rem 1.5rem;
      background: var(--p-primary-color, #6366f1);
      color: white;
    }
    .brand {
      font-size: 1.2rem;
      font-weight: 700;
      cursor: pointer;
    }
    .nav-links {
      display: flex;
      align-items: center;
      gap: 1.25rem;
    }
    .nav-links a {
      color: white;
      text-decoration: none;
      font-weight: 500;
    }
    .nav-links a:hover { text-decoration: underline; }
  `]
})
export class NavbarComponent {
  constructor(public auth: AuthService, private router: Router) {}

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
