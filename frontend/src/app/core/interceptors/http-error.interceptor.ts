import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable, throwError } from 'rxjs';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {

  const router = inject(Router);

  return next(req).pipe(
    catchError((error: any) => {
      if( error instanceof HttpErrorResponse && error.status === 0) {
        router.navigate(['/serverError']);
      }

      return throwError(() => error);
    })
  )



}
