import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from "@angular/router";
import { AuthService } from "../service/auth.service";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { UserRole } from "../../core/enums/user-role.enum";


@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
      if(!this.authService.isAuthenticated()) {
        this.router.navigate(['/login']);
        return false;
      }

      const requiredRoles = route.data['roles'] as UserRole[] | undefined;

      if(!requiredRoles || requiredRoles.length === 0) {
        return true;
      }

      const currentUser = this.authService.getUser();
      if(currentUser && requiredRoles.includes(currentUser.role)) {
        return true;
      }

      this.router.navigate(['/forbidden']);
      return false;

  }
}
