import { Injectable }               from '@angular/core';
import { HttpClient, HttpHeaders }  from '@angular/common/http';
import { Observable }               from 'rxjs/Observable';
import { Config }                   from '../../config';
import { UserService }              from '../user/user.service';

import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/map';

@Injectable()
export class RemoteService {
    url: string;
  constructor(public http: HttpClient, public UserServ: UserService) { 
    var me: any = this;
    me.url = Config.sharedServer.host + Config.sharedServer.prefix;
  }
  getHeaders():any{
    var me: any = this;
    return { headers: new HttpHeaders({
                'Content-Type':  'application/json'//,
            //    'authorization': 'Bearer ' + me.UserServ.getToken()
            })
    };
  }
  /*
    ex: endPoint 
    (GET/POST) '/servers' 
    (PUT/DELETE) `/servers/${id}` 
  */
  get(endPoint: string): Observable<any>{
    var me: any = this;
    return this.http.get<any>(me.url + endPoint, me.getHeaders());
  }
  post(endPoint :string, data: any): Observable<any> {
    var me: any = this;
    console.info(me.url + endPoint)
      return this.http.post<any>(me.url + endPoint, data, me.getHeaders());
  }
  put(endPoint :string, data: any): Observable<any> {
    var me: any = this;
      return this.http.put<any>(me.url + endPoint, data, me.getHeaders());
  }
  delete(endPoint :string): Observable<any> {
    var me: any = this;
      return this.http.delete<any>(me.url + endPoint, me.getHeaders());
  }
}
