import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { NotFoundComponent } from './pages/error/notFound/notFound.component';
import { EmployeesComponent } from './pages/employees/employees.component';
import { AuthGuard } from './auth/guard/auth.guard';
import { AdminComponent } from './pages/admin/admin.component';
import { UserRole } from './core/enums/user-role.enum';
import { ForbiddenComponent } from './pages/error/forbidden/forbidden.component';
import { UpdatePasswordComponent } from './pages/update-password/update-password.component';
import { DepartmentsComponent } from './pages/departments/departments.component';
import { PositionsComponent } from './pages/positions/positions.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ServerErrorComponent } from './pages/error/serverError/serverError.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'employees',
    component: EmployeesComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'departments',
    component: DepartmentsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'positions',
    component: PositionsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AuthGuard],
    data: {roles: [ UserRole.ADMIN, UserRole.MANAGER ] }
  },
  {
    path: 'updatePassword/:id',
    component: UpdatePasswordComponent,
    canActivate: [AuthGuard],
    data: { roles: [ UserRole.ADMIN ], self: true }
  },
  {
    path: 'forbidden',
    component: ForbiddenComponent
  },
  {
    path: 'serverError',
    component: ServerErrorComponent
  },
  {
    path: '**',
    component: NotFoundComponent
  },
];
