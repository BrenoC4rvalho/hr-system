export interface User {
  id: number;
  username: string;
  role: 'ADMIN' | 'MANAGER' | 'HR';
  status: 'ACTIVE' | 'INACTIVE';
  employee: null;
}
