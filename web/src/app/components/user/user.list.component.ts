import { Component, OnInit, EventEmitter, OnDestroy } from '@angular/core'
import { MaterializeDirective, MaterializeAction, toast} from "angular2-materialize";
import { User }             from '../../models/user'
import { UserService }      from '../../services/user/user.service'

declare var $ :any;
declare var M_Modal : any;

@Component({
    templateUrl: './user.list.component.html'
})
export class UserListComponent {
    title: string;
    users: Array<User>;
    public user:User = new User();
    constructor(public UserServ: UserService){
        this.users = []
        this.title = 'Users'
    }
    
    ngOnInit() {
      this.get()
    }

    private get(){
        
    }
}
