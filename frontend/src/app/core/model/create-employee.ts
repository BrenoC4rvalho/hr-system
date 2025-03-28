import { Gender } from "../enums/gender.enum";
import { Shift } from "../enums/shift.enum";
import { Department } from "./department";
import { Position } from "./position";

export interface CreateEmployee {
  firstName: string;
  lastName?: string;
  email?: string;
  phone: string;
  gender: Gender;
  department: Department;
  position: Position;
  shift?: Shift;
  birthDate: Date;
  hiredDate: Date;
}
