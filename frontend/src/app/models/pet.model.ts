export type AnimalType = 'UNKNOWN' | 'CAT' | 'DOG' | 'WOLF' | 'TIGER' | 'PANDA' | 'EAGLE' | 'RACOON';

export interface Pet {
  id?: number;
  name: string;
  description?: string;
  type: AnimalType;
}
