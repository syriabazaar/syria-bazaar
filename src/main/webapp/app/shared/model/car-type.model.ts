export interface ICarType {
  id?: number;
  typeName?: string;
}

export class CarType implements ICarType {
  constructor(
    public id?: number,
    public typeName?: string,
  ) {}
}
