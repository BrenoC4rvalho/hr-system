import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from "@angular/router";
import { AuthService } from "../service/auth.service";
import { Injectable } from "@angular/core";
import { catchError, map, Observable, of, switchMap, take } from "rxjs";
import { UserRole } from "../../core/enums/user-role.enum";
import { User } from "../../core/model/user";


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

    return this.authService.user$.pipe(
      take(1),
      switchMap((user) => {
        if (!user && this.authService.getToken()) {
          return this.authService.loadUser().pipe(
            map((loadedUser) => this.checkRoles(loadedUser, route))
          );
        }
        return of(this.checkRoles(user, route));
      }),
      catchError(() => {
        this.router.navigate(['/login']);
        return of(false);
      })
    );

  }

  private checkRoles(user: User | null, route: ActivatedRouteSnapshot): boolean {
    const requiredRoles = route.data['roles'] as UserRole[] | undefined;

    if (!requiredRoles || requiredRoles.length === 0) {
      return true;
    }

    if (user && requiredRoles.includes(user.role)) {
      return true;
    }

    this.router.navigate(['/forbidden']);
    return false;
  }

}

