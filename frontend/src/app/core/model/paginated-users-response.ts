import { User } from "./user";

export interface PaginatedUsersResponse {
  totalUsers: number;
  currentPage: number;
  totalPages: number;
  pageSize: number;
  users: User[]
}
