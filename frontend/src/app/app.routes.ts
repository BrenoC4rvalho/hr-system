import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { NotFoundComponent } from './pages/error/notFound/notFound.component';
import { EmployeesComponent } from './pages/employees/employees.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'employees', component: EmployeesComponent },
  { path: '**', component: NotFoundComponent },
];
