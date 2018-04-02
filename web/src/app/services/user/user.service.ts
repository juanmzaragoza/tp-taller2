import { Injectable } from '@angular/core'
import { User }       from '../../models/user';

@Injectable()
export class UserService {
    user: User;
    constructor(){
        this.user = undefined;
    }
    setUser = (u:User)=>{
        this.user = u;
    }
    getToken = ()=>{
        return this.user.getToken();
    }
}
