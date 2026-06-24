import { Component, DestroyRef, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, InputTextModule, PasswordModule, ButtonModule, MessageModule],
  template: `
    <div class="login-container">
      <div class="login-hero">
        <div class="hero-orb hero-orb-1"></div>
        <div class="hero-orb hero-orb-2"></div>
        <div class="hero-orb hero-orb-3"></div>
        <div class="hero-content">
          <div class="hero-brand">🐾 PetAdopt</div>
          <h1>Find your perfect companion</h1>
          <p>Thousands of loving pets are waiting for their forever home.
             Start your adoption journey today.</p>
          <div class="hero-animals">
            <span class="animal-bubble">🐶</span>
            <span class="animal-bubble">🐱</span>
            <span class="animal-bubble">🐦</span>
            <span class="animal-bubble">🐰</span>
          </div>
        </div>
      </div>

      <div class="login-form-side">
        <div class="form-wrapper">
          <h2>Welcome back</h2>
          <p class="form-subtitle">Sign in to your account to continue</p>

          @if (error) {
            <p-message severity="error" [text]="error" styleClass="w-full error-msg" />
          }

          <form [formGroup]="form" (ngSubmit)="submit()">
            <div class="field">
              <label for="email">Email address</label>
              <input pInputText id="email" type="email" formControlName="email"
                     placeholder="you@example.com" class="w-full" />
            </div>

            <div class="field">
              <label for="password">Password</label>
              <p-password id="password" formControlName="password" [feedback]="false"
                          placeholder="Enter your password"
                          styleClass="w-full" inputStyleClass="w-full" />
            </div>

            <p-button label="Sign in" icon="pi pi-arrow-right" iconPos="right"
                      [loading]="loading" [disabled]="form.invalid"
                      styleClass="w-full mt-2" type="submit" />
          </form>
        </div>
      </div>
    </div>
  `,
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private destroyRef = inject(DestroyRef);
  private fb = inject(FormBuilder);

  form = this.fb.nonNullable.group({
    email:    ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });
  loading = false;
  error = '';

  constructor(private auth: AuthService, private router: Router) {}

  submit(): void {
    if (this.form.invalid) return;
    this.error = '';
    this.loading = true;
    const { email, password } = this.form.getRawValue();
    this.auth.login(email, password)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => this.router.navigate(['/pets']),
        error: () => {
          this.error = 'Invalid email or password.';
          this.loading = false;
        }
      });
  }
}
