import { Component, OnInit } from '@angular/core';
import { Pet, AnimalType } from '../../models/pet.model';
import { PetService } from '../../services/pet.service';
import { AdoptionService } from '../../services/adoption.service';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

const ANIMAL_EMOJI: Record<AnimalType, string> = {
  DOG: '🐶', CAT: '🐱', BIRD: '🐦', RABBIT: '🐰', OTHER: '🐾'
};

@Component({
  selector: 'app-pet-list',
  imports: [CardModule, ButtonModule, ToastModule],
  providers: [MessageService],
  template: `
    <p-toast />
    <div class="page">
      <h2>Available Pets</h2>
      @if (loading) {
        <p>Loading...</p>
      } @else {
        <div class="pet-grid">
          @for (pet of pets; track pet.id) {
            <p-card [header]="emoji(pet) + ' ' + pet.name" styleClass="pet-card">
              <p class="type-badge">{{ pet.animalType }}</p>
              <p class="description">{{ pet.description || 'No description.' }}</p>
              <ng-template pTemplate="footer">
                <p-button label="Adopt" icon="pi pi-heart"
                          [loading]="adopting === pet.id"
                          (onClick)="adopt(pet)" />
              </ng-template>
            </p-card>
          } @empty {
            <p>No pets available right now.</p>
          }
        </div>
      }
    </div>
  `,
  styles: [`
    .page { padding: 2rem; }
    h2 { margin-bottom: 1.5rem; }
    .pet-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
      gap: 1.25rem;
    }
    .pet-card { height: 100%; }
    .type-badge {
      font-size: 0.75rem;
      font-weight: 700;
      text-transform: uppercase;
      color: var(--p-primary-color, #6366f1);
      margin: 0 0 0.5rem;
    }
    .description { font-size: 0.9rem; color: #555; }
  `]
})
export class PetListComponent implements OnInit {
  pets: Pet[] = [];
  loading = true;
  adopting: number | undefined;

  constructor(
    private petService: PetService,
    private adoptionService: AdoptionService,
    private toast: MessageService
  ) {}

  ngOnInit(): void {
    this.petService.getAll().subscribe({
      next: pets => { this.pets = pets; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  emoji(pet: Pet): string {
    return ANIMAL_EMOJI[pet.animalType] ?? '🐾';
  }

  adopt(pet: Pet): void {
    if (!pet.id) return;
    this.adopting = pet.id;
    this.adoptionService.adopt(pet.id).subscribe({
      next: () => {
        this.toast.add({ severity: 'success', summary: 'Adopted!', detail: `You adopted ${pet.name}` });
        this.adopting = undefined;
      },
      error: () => {
        this.toast.add({ severity: 'error', summary: 'Error', detail: 'Could not adopt pet.' });
        this.adopting = undefined;
      }
    });
  }
}
