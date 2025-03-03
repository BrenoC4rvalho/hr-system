import { UserRole } from "../enums/user-role.enum";
import { UserStatus } from "../enums/user-status.enum";

export interface User {
  id: number;
  username: string;
  role: UserRole;
  status: UserStatus;
  employee: null;
}
