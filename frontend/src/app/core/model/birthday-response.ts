import { EmployeeBirthday } from "./employee-birthday";

export interface BirthdaysResponse {

  month: string;
  monthNumber: number;
  totalEmployees: number;
  employees: EmployeeBirthday[];

}
