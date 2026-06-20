import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'pets', pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent) },
  { path: 'pets', loadComponent: () => import('./pages/pets/pet-list.component').then(m => m.PetListComponent), canActivate: [authGuard] },
  { path: 'admin/pets', loadComponent: () => import('./pages/admin/admin-pets.component').then(m => m.AdminPetsComponent), canActivate: [authGuard, adminGuard] },
  { path: '**', redirectTo: 'pets' },
];
