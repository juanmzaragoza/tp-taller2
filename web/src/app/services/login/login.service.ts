import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { User } from '../../models/user'
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/map'

@Injectable()
export class LoginService {

  constructor(public http: HttpClient) { 
  
  }
  token(user :User): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    //return Observable.of(true).delay(1000);
    //const req = this.http.post('localhost:8081/api/v1/token', {
      return this.http.post<any>('http://localhost:8081/api/v1/token', 
      user, httpOptions).map(response => response.json());
  }
}
