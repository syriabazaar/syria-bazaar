export interface ISeller {
  id?: number;
  name?: string;
  address?: string;
  mobileNo?: string | null;
  whatsApp?: string | null;
  facebook?: string | null;
  insta?: string | null;
}

export class Seller implements ISeller {
  constructor(
    public id?: number,
    public name?: string,
    public address?: string,
    public mobileNo?: string | null,
    public whatsApp?: string | null,
    public facebook?: string | null,
    public insta?: string | null,
  ) {}
}
