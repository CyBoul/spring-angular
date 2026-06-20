import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MessageModule } from 'primeng/message';

@Component({
  selector: 'app-login',
  imports: [FormsModule, InputTextModule, PasswordModule, ButtonModule, CardModule, MessageModule],
  template: `
    <div class="login-wrapper">
      <p-card header="Sign in to PetAdopt" styleClass="login-card">
        @if (error) {
          <p-message severity="error" [text]="error" styleClass="w-full mb-3" />
        }
        <div class="field">
          <label for="email">Email</label>
          <input pInputText id="email" type="email" [(ngModel)]="email"
                 placeholder="you@example.com" class="w-full" />
        </div>
        <div class="field">
          <label for="password">Password</label>
          <p-password id="password" [(ngModel)]="password" [feedback]="false"
                      placeholder="Password" styleClass="w-full" inputStyleClass="w-full" />
        </div>
        <p-button label="Sign in" [loading]="loading" styleClass="w-full mt-2"
                  (onClick)="submit()" />
      </p-card>
    </div>
  `,
  styles: [`
    .login-wrapper {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: #f8f9fa;
    }
    .login-card { width: 380px; }
    .field { display: flex; flex-direction: column; gap: 0.4rem; margin-bottom: 1rem; }
    label { font-weight: 500; font-size: 0.9rem; }
  `]
})
export class LoginComponent {
  email = '';
  password = '';
  loading = false;
  error = '';

  constructor(private auth: AuthService, private router: Router) {}

  submit(): void {
    this.error = '';
    this.loading = true;
    this.auth.login(this.email, this.password).subscribe({
      next: () => this.router.navigate(['/pets']),
      error: () => {
        this.error = 'Invalid email or password.';
        this.loading = false;
      }
    });
  }
}
