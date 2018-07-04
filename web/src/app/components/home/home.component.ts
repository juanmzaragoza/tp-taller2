import { Component, OnInit }    from '@angular/core'
import { UserService }  from '../../services/user/user.service';

@Component({
    templateUrl: './home.component.html'
})
export class HomeComponent {
    public title:string;
    constructor(public UserServ: UserService){}
    
    ngOnInit() {
        this.title = 'Home'
      }

}
