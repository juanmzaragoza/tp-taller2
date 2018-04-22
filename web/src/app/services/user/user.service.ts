import { Injectable } from '@angular/core'
import { User }       from '../../models/user';
import { CookieService } from 'ngx-cookie-service';

@Injectable()
export class UserService {
    user: User;
    constructor(public cookieService: CookieService){
        this.user = undefined;
        if(this.cookieService.check('user')){
            this.user = JSON.parse(this.cookieService.get('user'));
        }
    }
    setUser = (u:User)=>{
        this.cookieService.set('user', JSON.stringify(u));
        this.user = u;
    }
    getUser = ()=>{
        if(this.cookieService.check('user')){
            this.user = JSON.parse(this.cookieService.get('user'));
            return this.user;
        }
        return undefined;
    }
    getToken = ()=>{
        var u = this.getUser();
        if(u){ 
            return u.token;
        }
        return undefined;
    }
    clean = () =>{
        this.cookieService.delete('user');
        this.user = undefined;
    }
}
