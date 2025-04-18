import { type IBrand } from '@/shared/model/brand.model';

export interface ICarModel {
  id?: number;
  name?: string;
  brand?: IBrand | null;
}

export class CarModel implements ICarModel {
  constructor(
    public id?: number,
    public name?: string,
    public brand?: IBrand | null,
  ) {}
}
