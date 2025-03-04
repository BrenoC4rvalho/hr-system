import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { take, switchMap, map, catchError } from 'rxjs/operators';
import { AuthService } from '../service/auth.service';
import { User } from '../../core/model/user';
import { UserStatus } from '../../core/enums/user-status.enum';
import { UserRole } from '../../core/enums/user-role.enum';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | boolean {
    if (!this.authService.isAuthenticated()) {
      this.redirectToLogin();
      return false;
    }

    return this.authService.user$.pipe(
      take(1),
      switchMap((user: User | null) => this.handleUser(user, route)),
      catchError((error) => this.handleError(error, state))
    );
  }

  private handleUser(user: User | null, route: ActivatedRouteSnapshot): Observable<boolean> {
    if (!user && this.authService.getToken()) {
      return this.authService.loadUser().pipe(
        map((loadedUser: User) => this.validateUser(loadedUser, route))
      );
    }
    return of(this.validateUser(user, route));
  }

  private validateUser(user: User | null, route: ActivatedRouteSnapshot): boolean {
    if (this.checkStatusInactive(user)) {
      this.authService.logout();
      this.redirectToLogin();
      return false;
    }
    if (!this.checkRoles(user, route)) {
      this.router.navigate(['/forbidden']);
      return false;
    }
    return true;
  }

  private checkStatusInactive(user: User | null): boolean {
    return user?.status === UserStatus.INACTIVE;
  }

  private checkRoles(user: User | null, route: ActivatedRouteSnapshot): boolean {
    const requiredRoles = route.data['roles'] as UserRole[] | undefined;

    if (!requiredRoles || requiredRoles.length === 0) {
      return true;
    }

    if (!user || !user.role || !requiredRoles.includes(user.role)) {
      this.router.navigate(['/forbidden']);
      return false;
    }

    return true;
  }

  private redirectToLogin(): void {
    this.router.navigate(['/login']);
  }

  private handleError(error: any, state: RouterStateSnapshot): Observable<boolean> {
    console.error('AuthGuard error:', error);
    this.authService.logout();
    this.redirectToLogin();
    return of(false);
  }

}
