import { Employee } from "./employee";

export interface PaginatedEmployeesResponse {
  totalEmployees: number;
  currentPage: number;
  totalPages: number;
  pageSize: number;
  employees: Employee[]
}
