import { Component, OnInit }    from '@angular/core'
import { UserService }  from '../../services/user/user.service';

@Component({
    templateUrl: './home.component.html'
})
export class HomeComponent {
    hi:string;
    constructor(public UserServ: UserService){}
    
    ngOnInit() {
        this.hi = ''
        if(this.UserServ.user && this.UserServ.user.username){
            this.hi = 'Hola ' + this.UserServ.user.username + ', estas en la Home.'
        }
      }

}
