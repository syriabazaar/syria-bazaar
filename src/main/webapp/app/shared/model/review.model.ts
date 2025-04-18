import { type ISeller } from '@/shared/model/seller.model';

export interface IReview {
  id?: number;
  createdAt?: Date;
  revVal?: number | null;
  revTxt?: string | null;
  fromUser?: ISeller | null;
  toUser?: ISeller | null;
}

export class Review implements IReview {
  constructor(
    public id?: number,
    public createdAt?: Date,
    public revVal?: number | null,
    public revTxt?: string | null,
    public fromUser?: ISeller | null,
    public toUser?: ISeller | null,
  ) {}
}
