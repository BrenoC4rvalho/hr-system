import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from "@angular/router";
import { AuthService } from "../service/auth.service";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
      if(this.authService.isAuthenticated()) {
        return true;
      }
      this.router.navigate(['/login']);
      return false;
  }
}
