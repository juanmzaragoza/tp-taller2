import { Injectable }               from '@angular/core';
import { HttpClient, HttpHeaders }  from '@angular/common/http';
import { Observable }               from 'rxjs/Observable';
import { User }                     from '../../models/user';
import { Config }                   from '../../config';
import { UserService }              from '../user/user.service';

import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/map';

@Injectable()
export class LoginService {

  constructor(public http: HttpClient, public UserServ: UserService) { 
  
  }
  token(user :User): Observable<boolean> {
    let url = Config.sharedServer.host + 
              Config.sharedServer.prefix + 
              Config.sharedServer.endPoint.token;
    const httpOptions = {
        headers: new HttpHeaders({
          'Content-Type':  'application/json'
        })
      };
      return this.http.post<Boolean>(url, user, httpOptions).map(
        res =>{
            delete user.password;
            user.token = res["token"]["token"]
            this.UserServ.setUser(user);
            console.info(res);
            return true;
        });
  }
}
