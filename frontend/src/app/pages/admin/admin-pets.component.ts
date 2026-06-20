import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Pet, AnimalType } from '../../models/pet.model';
import { PetService } from '../../services/pet.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';

const ANIMAL_TYPES: { label: string; value: AnimalType }[] = [
  { label: 'Dog', value: 'DOG' },
  { label: 'Cat', value: 'CAT' },
  { label: 'Bird', value: 'BIRD' },
  { label: 'Rabbit', value: 'RABBIT' },
  { label: 'Other', value: 'OTHER' },
];

@Component({
  selector: 'app-admin-pets',
  imports: [
    FormsModule,
    TableModule, ButtonModule, DialogModule, ConfirmDialogModule,
    InputTextModule, TextareaModule, SelectModule, ToastModule
  ],
  providers: [ConfirmationService, MessageService],
  template: `
    <p-toast />
    <p-confirmDialog />
    <div class="page">
      <div class="page-header">
        <h2>Pet Management</h2>
        <p-button label="Add Pet" icon="pi pi-plus" (onClick)="openNew()" />
      </div>

      <p-table [value]="pets" [loading]="loading" styleClass="p-datatable-sm">
        <ng-template pTemplate="header">
          <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Description</th>
            <th style="width:140px"></th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-pet>
          <tr>
            <td>{{ pet.name }}</td>
            <td>{{ pet.animalType }}</td>
            <td>{{ pet.description }}</td>
            <td>
              <p-button icon="pi pi-pencil" severity="secondary" size="small"
                        [text]="true" (onClick)="openEdit(pet)" />
              <p-button icon="pi pi-trash" severity="danger" size="small"
                        [text]="true" (onClick)="confirmDelete(pet)" />
            </td>
          </tr>
        </ng-template>
      </p-table>
    </div>

    <p-dialog [(visible)]="dialogVisible"
              [header]="editingPet?.id ? 'Edit Pet' : 'New Pet'"
              [modal]="true" [style]="{ width: '400px' }">
      <div class="field">
        <label>Name</label>
        <input pInputText [(ngModel)]="form.name" class="w-full" />
      </div>
      <div class="field">
        <label>Type</label>
        <p-select [options]="animalTypes" [(ngModel)]="form.animalType"
                  optionLabel="label" optionValue="value" styleClass="w-full" />
      </div>
      <div class="field">
        <label>Description</label>
        <textarea pTextarea [(ngModel)]="form.description" rows="3" class="w-full"></textarea>
      </div>
      <ng-template pTemplate="footer">
        <p-button label="Cancel" severity="secondary" (onClick)="dialogVisible = false" />
        <p-button label="Save" [loading]="saving" (onClick)="save()" />
      </ng-template>
    </p-dialog>
  `,
  styles: [`
    .page { padding: 2rem; }
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; }
    h2 { margin: 0; }
    .field { display: flex; flex-direction: column; gap: 0.4rem; margin-bottom: 1rem; }
    label { font-weight: 500; font-size: 0.9rem; }
  `]
})
export class AdminPetsComponent implements OnInit {
  pets: Pet[] = [];
  loading = true;
  dialogVisible = false;
  saving = false;
  editingPet: Pet | null = null;
  form: Partial<Pet> = {};
  animalTypes = ANIMAL_TYPES;

  constructor(
    private petService: PetService,
    private confirmService: ConfirmationService,
    private toast: MessageService
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.petService.getAll().subscribe({
      next: pets => { this.pets = pets; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  openNew(): void {
    this.editingPet = null;
    this.form = { animalType: 'DOG' };
    this.dialogVisible = true;
  }

  openEdit(pet: Pet): void {
    this.editingPet = pet;
    this.form = { ...pet };
    this.dialogVisible = true;
  }

  save(): void {
    if (!this.form.name || !this.form.animalType) return;
    this.saving = true;
    const pet = this.form as Pet;
    const op = this.editingPet?.id
      ? this.petService.update(this.editingPet.id, pet)
      : this.petService.create(pet);

    op.subscribe({
      next: () => {
        this.toast.add({ severity: 'success', summary: 'Saved', detail: 'Pet saved.' });
        this.dialogVisible = false;
        this.saving = false;
        this.load();
      },
      error: () => {
        this.toast.add({ severity: 'error', summary: 'Error', detail: 'Could not save pet.' });
        this.saving = false;
      }
    });
  }

  confirmDelete(pet: Pet): void {
    this.confirmService.confirm({
      message: `Delete "${pet.name}"?`,
      accept: () => this.delete(pet)
    });
  }

  delete(pet: Pet): void {
    if (!pet.id) return;
    this.petService.delete(pet.id).subscribe({
      next: () => {
        this.toast.add({ severity: 'success', summary: 'Deleted', detail: `${pet.name} removed.` });
        this.load();
      },
      error: () => {
        this.toast.add({ severity: 'error', summary: 'Error', detail: 'Could not delete pet.' });
      }
    });
  }
}
