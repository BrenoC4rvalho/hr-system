import { UserRole } from "../enums/user-role.enum";

export interface User {
  id: number;
  username: string;
  role: UserRole;
  status: 'ACTIVE' | 'INACTIVE';
  employee: null;
}
