import { Employee } from "./employee";

export interface Department {

  id: number;
  name: string;
  manager?: Employee;
  numberOfEmployees: number;

}
