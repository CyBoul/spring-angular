import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
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
  { label: 'Wolf', value: 'WOLF' },
  { label: 'Tiger', value: 'TIGER' },
  { label: 'Panda', value: 'PANDA' },
  { label: 'Eagle', value: 'EAGLE' },
  { label: 'Racoon', value: 'RACOON' },
  { label: 'Unknown', value: 'UNKNOWN' },
];

@Component({
  selector: 'app-admin-pets',
  imports: [
    ReactiveFormsModule,
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
            <td>{{ pet.type }}</td>
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
      <form [formGroup]="form">
        <div class="field">
          <label>Name</label>
          <input pInputText formControlName="name" class="w-full" />
        </div>
        <div class="field">
          <label>Type</label>
          <p-select [options]="types" formControlName="type"
                    optionLabel="label" optionValue="value" styleClass="w-full" />
        </div>
        <div class="field">
          <label>Description</label>
          <textarea pTextarea formControlName="description" rows="3" class="w-full"></textarea>
        </div>
      </form>
      <ng-template pTemplate="footer">
        <p-button label="Cancel" severity="secondary" (onClick)="dialogVisible = false" />
        <p-button label="Save" [loading]="saving" [disabled]="form.invalid" (onClick)="save()" />
      </ng-template>
    </p-dialog>
  `,
  styleUrl: './admin-pets.component.scss'
})
export class AdminPetsComponent implements OnInit {
  private destroyRef = inject(DestroyRef);
  private fb = inject(FormBuilder);

  pets: Pet[] = [];
  loading = true;
  dialogVisible = false;
  saving = false;
  editingPet: Pet | null = null;
  types = ANIMAL_TYPES;

  form = this.fb.nonNullable.group({
    name:        ['', [Validators.required, Validators.maxLength(50)]],
    type:        ['DOG' as AnimalType, [Validators.required]],
    description: ['', [Validators.maxLength(300)]],
  });

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
    this.petService.getAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: pets => { this.pets = pets; this.loading = false; },
        error: () => { this.loading = false; }
      });
  }

  openNew(): void {
    this.editingPet = null;
    this.form.reset({ name: '', type: 'DOG', description: '' });
    this.dialogVisible = true;
  }

  openEdit(pet: Pet): void {
    this.editingPet = pet;
    this.form.patchValue({
      name: pet.name,
      type: pet.type,
      description: pet.description ?? '',
    });
    this.dialogVisible = true;
  }

  save(): void {
    if (this.form.invalid) return;
    this.saving = true;
    const pet = this.form.getRawValue() as Pet;
    const op = this.editingPet?.id
      ? this.petService.update(this.editingPet.id, pet)
      : this.petService.create(pet);

    op.pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
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
    this.petService.delete(pet.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
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
