import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { Pet, AnimalType } from '../../models/pet.model';
import { PetService } from '../../services/pet.service';
import { AdoptionService } from '../../services/adoption.service';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

const ANIMAL_EMOJI: Record<AnimalType, string> = {
  DOG: '🐶', CAT: '🐱', WOLF: '🐺', TIGER: '🐯',
  PANDA: '🐼', EAGLE: '🦅', RACOON: '🦝', UNKNOWN: '🐾'
};

const ACCENT: Record<AnimalType, { color: string; bg: string }> = {
  DOG:     { color: '#f59e0b', bg: '#fef3c7' },
  CAT:     { color: '#8b5cf6', bg: '#ede9fe' },
  WOLF:    { color: '#64748b', bg: '#f1f5f9' },
  TIGER:   { color: '#ea580c', bg: '#fff7ed' },
  PANDA:   { color: '#059669', bg: '#d1fae5' },
  EAGLE:   { color: '#0284c7', bg: '#e0f2fe' },
  RACOON:  { color: '#78716c', bg: '#f5f5f4' },
  UNKNOWN: { color: '#6366f1', bg: '#e0e7ff' },
};

type FilterType = AnimalType | 'ALL';

interface FilterChip {
  label: string;
  value: FilterType;
}

const FILTER_CHIPS: FilterChip[] = [
  { label: 'All',     value: 'ALL' },
  { label: '🐶 Dogs',    value: 'DOG' },
  { label: '🐱 Cats',    value: 'CAT' },
  { label: '🐺 Wolves',  value: 'WOLF' },
  { label: '🐯 Tigers',  value: 'TIGER' },
  { label: '🐼 Pandas',  value: 'PANDA' },
  { label: '🦅 Eagles',  value: 'EAGLE' },
  { label: '🦝 Racoons', value: 'RACOON' },
];

@Component({
  selector: 'app-pet-list',
  imports: [FormsModule, ButtonModule, ToastModule],
  providers: [MessageService],
  template: `
    <p-toast />

    <div class="pets-page">
      <div class="hero">
        <h1>Find Your New Best Friend</h1>
        <p>Browse our lovely pets and give them a forever home</p>
      </div>

      <div class="content">
        <!-- toolbar -->
        <div class="toolbar">
          <div class="search-box">
            <i class="pi pi-search"></i>
            <input type="text" placeholder="Search pets by name..."
                   [(ngModel)]="searchTerm" (input)="applyFilters()" />
          </div>
          <div class="chips">
            @for (chip of filterChips; track chip.value) {
              <button [class.active]="selectedType === chip.value"
                      (click)="setFilter(chip.value)">{{ chip.label }}</button>
            }
          </div>
        </div>

        <!-- loading skeleton -->
        @if (loading) {
          <div class="grid">
            @for (_ of skeletons; track $index) {
              <div class="card skel-card">
                <div class="card-accent skel-bar"></div>
                <div class="card-body">
                  <div class="skel skel-emoji"></div>
                  <div class="skel skel-title"></div>
                  <div class="skel skel-badge"></div>
                  <div class="skel skel-text"></div>
                  <div class="skel skel-text short"></div>
                </div>
              </div>
            }
          </div>
        } @else {
          <div class="grid">
            @for (pet of filteredPets; track pet.id) {
              <div class="card">
                <div class="card-accent" [style.background]="accent(pet).color"></div>
                <div class="card-body">
                  <div class="card-emoji">{{ emoji(pet) }}</div>
                  <h3 class="card-name">{{ pet.name }}</h3>
                  <span class="card-type"
                        [style.color]="accent(pet).color"
                        [style.background]="accent(pet).bg">
                    {{ pet.type }}
                  </span>
                  <p class="card-desc">{{ pet.description || 'A lovely pet looking for a home.' }}</p>
                </div>
                <div class="card-footer">
                  <p-button label="Adopt" icon="pi pi-heart"
                            [loading]="adopting === pet.id"
                            (onClick)="adopt(pet)" styleClass="w-full" />
                </div>
              </div>
            } @empty {
              <div class="empty">
                @if (searchTerm || selectedType !== 'ALL') {
                  <div class="empty-icon">🔍</div>
                  <h3>No pets found</h3>
                  <p>Try adjusting your search or filter</p>
                } @else {
                  <div class="empty-icon">🐾</div>
                  <h3>No pets available</h3>
                  <p>Check back soon for new furry friends!</p>
                }
              </div>
            }
          </div>
        }
      </div>
    </div>
  `,
  styleUrl: './pet-list.component.scss'
})
export class PetListComponent implements OnInit {
  private destroyRef = inject(DestroyRef);

  pets: Pet[] = [];
  filteredPets: Pet[] = [];
  loading = true;
  adopting: number | undefined;
  searchTerm = '';
  selectedType: FilterType = 'ALL';
  filterChips = FILTER_CHIPS;
  skeletons = Array(6).fill(0);

  constructor(
    private petService: PetService,
    private adoptionService: AdoptionService,
    private toast: MessageService
  ) {}

  ngOnInit(): void {
    this.petService.getAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: pets => {
          this.pets = pets;
          this.filteredPets = pets;
          this.loading = false;
        },
        error: () => { this.loading = false; }
      });
  }

  emoji(pet: Pet): string {
    return ANIMAL_EMOJI[pet.type] ?? '🐾';
  }

  accent(pet: Pet) {
    return ACCENT[pet.type] ?? ACCENT['UNKNOWN'];
  }

  applyFilters(): void {
    let result = this.pets;
    if (this.selectedType !== 'ALL') {
      result = result.filter(p => p.type === this.selectedType);
    }
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(p => p.name.toLowerCase().includes(term));
    }
    this.filteredPets = result;
  }

  setFilter(type: FilterType): void {
    this.selectedType = type;
    this.applyFilters();
  }

  adopt(pet: Pet): void {
    if (!pet.id) return;
    this.adopting = pet.id;
    this.adoptionService.adopt(pet.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
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
