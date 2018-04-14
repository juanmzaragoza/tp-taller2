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
    getUser = ()=>{
        return this.user;
    }
    getToken = ()=>{
        return this.user.getToken();
    }
    clean = () =>{
        this.user = undefined;
    }
}
