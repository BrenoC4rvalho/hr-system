import { UserRole } from "../enums/user-role.enum";
import { UserStatus } from "../enums/user-status.enum";
import { Employee } from "./employee";

export interface User {
  id: number;
  username: string;
  role: UserRole;
  status: UserStatus;
  employee?: Employee;
  employeeId?: number
}
