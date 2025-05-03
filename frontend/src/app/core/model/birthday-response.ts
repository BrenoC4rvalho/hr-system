import { EmployeeBasic } from "./employee-basic";

export interface BirthdaysResponse {

  month: string;
  monthNumber: number;
  totalEmployees: number;
  employees: EmployeeBasic[];

}
