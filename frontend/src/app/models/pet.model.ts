export type AnimalType = 'DOG' | 'CAT' | 'BIRD' | 'RABBIT' | 'OTHER';

export interface Pet {
  id?: number;
  name: string;
  description?: string;
  animalType: AnimalType;
}
