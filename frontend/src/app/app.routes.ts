import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { NotFoundComponent } from './pages/error/notFound/notFound.component';
import { EmployeesComponent } from './pages/employees/employees.component';
import { AuthGuard } from './auth/guard/auth.guard';
import { AdminComponent } from './pages/admin/admin.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'employees', component: EmployeesComponent, canActivate: [AuthGuard] },
  { path: 'admin', component: AdminComponent, canActivate: [AuthGuard]},
  { path: '**', component: NotFoundComponent },
];
