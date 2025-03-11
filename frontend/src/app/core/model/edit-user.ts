import { UserRole } from "../enums/user-role.enum";
import { UserStatus } from "../enums/user-status.enum";

export interface EditUser {

  username?: string;
  role?: UserRole;
  status?: UserStatus
  employeeId?: number;

}
