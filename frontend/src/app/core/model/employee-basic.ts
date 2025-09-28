import { Department } from "./department";

export interface EmployeeBasic {

  id: number;
  firstName: number;
  lastName: number;
  department: Department;
  birthDate: Date;
  hiredDate: Date;

}
