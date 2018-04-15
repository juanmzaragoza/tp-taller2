import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { UserService } from '../user/user.service';
import { Observable } from 'rxjs/Observable';
@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(public UserServ: UserService) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if(request.body.password != 'root'){
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.UserServ.getToken()}`
        }
      });
      console.info(request)
      return next.handle(request);
    }
    else{
      return next.handle(request)
    }
  }
}
