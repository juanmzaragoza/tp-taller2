import { Component }    from '@angular/core';
import { Router }       from '@angular/router';
import { LoginService } from '../../services/login/login.service'
import { User }         from '../../models/user'
import { Observable }   from 'rxjs/Observable';
import { Location }     from '@angular/common';
import { SharedService } from '../../services/common/shared.service'

@Component({
    templateUrl: './login.component.html'
})
export class LoginComponent {
    private user:User = new User('','');
    constructor(public loginServ :LoginService, 
                public RouterServ : Router,
                public Location: Location,
                public SharedServ: SharedService){}

    login(user: User) {
        this.loginServ.token(user).subscribe((val) => {
          if(val){
                this.SharedServ.startAccount.emit(user.username);
                this.RouterServ.navigate(['/home']);
          }
          else{
                console.info('credencial incorrecta');
          }
        });
      }
      back(){
        //this.Location.back();
        this.RouterServ.navigate(['/home']);
      }
}

