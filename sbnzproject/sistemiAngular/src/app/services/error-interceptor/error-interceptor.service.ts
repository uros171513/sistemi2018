import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpRequest, HttpHandler, HttpErrorResponse, HttpEvent } from '@angular/common/http';
import { Observable, throwError } from 'rxjs'
import { catchError } from 'rxjs/operators';

@Injectable()
export class ErrorInterceptorService {

  constructor(private router:Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError(errorReponse => {
        let errMsg: string;
        if (errorReponse instanceof HttpErrorResponse) {
            const err = errorReponse.message || JSON.stringify(errorReponse.error);
            const status=`${errorReponse.status}`;
            console.log(status);
            if(status==="404"){
              this.router.navigate(['/not-found']);
            }
            errMsg = `${errorReponse.status} - ${errorReponse.statusText || ''} Details: ${err}`;
        } else {
            errMsg = errorReponse.message ? errorReponse.message : errorReponse.toString();
        }
        return throwError(errMsg);
    }));
  }
}
