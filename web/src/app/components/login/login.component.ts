import { Component }    from '@angular/core';
import { Router }       from '@angular/router';
import { LoginService } from '../../services/login/login.service'
import { User }         from '../../models/user'
import { Observable }   from 'rxjs/Observable';
import { Location }     from '@angular/common';

@Component({
    templateUrl: './login.component.html'
})
export class LoginComponent {
    private user:User = new User('','');
    constructor(public loginServ :LoginService, 
                public RouterServ : Router,
                public Location: Location){}

    login(user: User) {
        this.loginServ.token(user).subscribe((val) => {
          if(val){
              console.log('logeado');
              console.log('User: ' + user.username);
              console.log('pass: ' + user.password);
              console.info(val);
              this.RouterServ.navigate(['/home']);
          }
          else{
              console.info('credencial incorrecta');
          }
        });
      }
      back(){
        this.Location.back();
      }
}

