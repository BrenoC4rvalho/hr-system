import { UserRole } from "../enums/user-role.enum";

export interface CreateUser {
  username: string;
  role: UserRole;
  employeeId: number | null;
}
