import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutDashboard, LucideAngularModule, Users, ShieldUser } from 'lucide-angular';

@Component({
  selector: 'app-navbar',
  imports: [RouterModule, LucideAngularModule],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent {

  readonly EmployeeIcon = Users;
  readonly DashboardIcon = LayoutDashboard;
  readonly AdminIcon = ShieldUser;

}
