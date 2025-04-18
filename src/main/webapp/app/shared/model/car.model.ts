import { type IBrand } from '@/shared/model/brand.model';
import { type ISeller } from '@/shared/model/seller.model';
import { type ICarType } from '@/shared/model/car-type.model';
import { type ICarModel } from '@/shared/model/car-model.model';
import { type ICity } from '@/shared/model/city.model';

export interface ICar {
  id?: number;
  year?: number | null;
  price?: number | null;
  mileage?: number | null;
  drivetrain?: string | null;
  engine?: string | null;
  transmission?: string | null;
  fuelType?: string | null;
  exteriorColor?: string | null;
  interiorColor?: string | null;
  vin?: string | null;
  location?: string | null;
  description?: string | null;
  publishedDate?: Date | null;
  adNumber?: number | null;
  views?: number | null;
  alloyWheels?: boolean | null;
  sunroof?: boolean | null;
  tintedGlass?: boolean | null;
  ledHeadlights?: boolean | null;
  foldableRoof?: boolean | null;
  towHitch?: boolean | null;
  adjustableSteeringWheel?: boolean | null;
  autoDimmingRearview?: boolean | null;
  heatedFrontSeats?: boolean | null;
  leatherSeats?: boolean | null;
  blindSpotMonitor?: boolean | null;
  adaptiveCruiseControl?: boolean | null;
  navigationSystem?: boolean | null;
  backupCamera?: boolean | null;
  appleCarplay?: boolean | null;
  androidAuto?: boolean | null;
  premiumSoundSystem?: boolean | null;
  isFirstOwn?: boolean | null;
  isAccedFree?: boolean | null;
  brand?: IBrand | null;
  seller?: ISeller | null;
  type?: ICarType | null;
  model?: ICarModel | null;
  city?: ICity | null;
}

export class Car implements ICar {
  constructor(
    public id?: number,
    public year?: number | null,
    public price?: number | null,
    public mileage?: number | null,
    public drivetrain?: string | null,
    public engine?: string | null,
    public transmission?: string | null,
    public fuelType?: string | null,
    public exteriorColor?: string | null,
    public interiorColor?: string | null,
    public vin?: string | null,
    public location?: string | null,
    public description?: string | null,
    public publishedDate?: Date | null,
    public adNumber?: number | null,
    public views?: number | null,
    public alloyWheels?: boolean | null,
    public sunroof?: boolean | null,
    public tintedGlass?: boolean | null,
    public ledHeadlights?: boolean | null,
    public foldableRoof?: boolean | null,
    public towHitch?: boolean | null,
    public adjustableSteeringWheel?: boolean | null,
    public autoDimmingRearview?: boolean | null,
    public heatedFrontSeats?: boolean | null,
    public leatherSeats?: boolean | null,
    public blindSpotMonitor?: boolean | null,
    public adaptiveCruiseControl?: boolean | null,
    public navigationSystem?: boolean | null,
    public backupCamera?: boolean | null,
    public appleCarplay?: boolean | null,
    public androidAuto?: boolean | null,
    public premiumSoundSystem?: boolean | null,
    public isFirstOwn?: boolean | null,
    public isAccedFree?: boolean | null,
    public brand?: IBrand | null,
    public seller?: ISeller | null,
    public type?: ICarType | null,
    public model?: ICarModel | null,
    public city?: ICity | null,
  ) {
    this.alloyWheels = this.alloyWheels ?? false;
    this.sunroof = this.sunroof ?? false;
    this.tintedGlass = this.tintedGlass ?? false;
    this.ledHeadlights = this.ledHeadlights ?? false;
    this.foldableRoof = this.foldableRoof ?? false;
    this.towHitch = this.towHitch ?? false;
    this.adjustableSteeringWheel = this.adjustableSteeringWheel ?? false;
    this.autoDimmingRearview = this.autoDimmingRearview ?? false;
    this.heatedFrontSeats = this.heatedFrontSeats ?? false;
    this.leatherSeats = this.leatherSeats ?? false;
    this.blindSpotMonitor = this.blindSpotMonitor ?? false;
    this.adaptiveCruiseControl = this.adaptiveCruiseControl ?? false;
    this.navigationSystem = this.navigationSystem ?? false;
    this.backupCamera = this.backupCamera ?? false;
    this.appleCarplay = this.appleCarplay ?? false;
    this.androidAuto = this.androidAuto ?? false;
    this.premiumSoundSystem = this.premiumSoundSystem ?? false;
    this.isFirstOwn = this.isFirstOwn ?? false;
    this.isAccedFree = this.isAccedFree ?? false;
  }
}
