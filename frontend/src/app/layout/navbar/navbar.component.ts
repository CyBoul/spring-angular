import { Component, DestroyRef, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive, ButtonModule],
  template: `
    <nav class="navbar">
      <span class="brand" routerLink="/pets">🐾 PetAdopt</span>
      <div class="nav-links">
        <a routerLink="/pets" routerLinkActive="active"
           [routerLinkActiveOptions]="{ exact: true }">Pets</a>
        @if (auth.isAdmin()) {
          <a routerLink="/admin/pets" routerLinkActive="active">Admin</a>
        }
        <p-button label="Logout" severity="secondary" size="small"
                  icon="pi pi-sign-out" (onClick)="logout()" />
      </div>
    </nav>
  `,
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  private destroyRef = inject(DestroyRef);

  constructor(public auth: AuthService, private router: Router) {}

  logout(): void {
    this.auth.logout()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.router.navigate(['/login']));
  }
}
