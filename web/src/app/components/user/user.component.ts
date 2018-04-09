import { Component }    from '@angular/core'
import { Router }       from '@angular/router'
import { UserService }  from '../../services/user/user.service'
import { User }         from '../../models/user'
import { SharedService } from '../../services/common/shared.service'

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html'
})
export class UserComponent {
    name :string
    showUser: boolean
    constructor(public RouterServ : Router,
                public UserServ: UserService,
                public SharedServ: SharedService){ 
        var me = this;
        this.name = 'Log In'
        this.showUser = false
        this.SharedServ.startAccount.subscribe(
            (data: any) => { me.logIn() });
    }
    logIn(){
        var usr = this.UserServ.getUser()
        this.name = usr.username
        this.showUser = true
    }
    logOut(){
        this.UserServ.clean()
        this.name = 'Log In'
        this.showUser = false
        this.RouterServ.navigate(['/login']);
    }
}
