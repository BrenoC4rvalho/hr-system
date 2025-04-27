import { EmployeeStatus } from "../enums/employee-status.enum";
import { Gender } from "../enums/gender.enum";
import { Shift } from "../enums/shift.enum";
import { Department } from "./department";
import { Position } from "./position";

export interface Employee {

  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  gender: Gender,
  department: Department;
  position: Position;
  shift: Shift;
  status: EmployeeStatus;
  birthDate: Date;
  hiredDate: Date;
  terminationDate: Date;

}
