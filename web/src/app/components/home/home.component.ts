import { Component, OnInit }    from '@angular/core'
import { UserService }  from '../../services/user/user.service';

@Component({
    templateUrl: './home.component.html'
})
export class HomeComponent {
    username:string;
    constructor(public UserServ: UserService){}
    
    ngOnInit() {
        this.username = this.UserServ.user.username;
      }

}
