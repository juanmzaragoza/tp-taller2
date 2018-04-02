import { Component } from '@angular/core';
import { LoginService } from '../../services/login/login.service'
import { User } from '../../models/user'
import { Observable } from 'rxjs/Observable';

@Component({
    templateUrl: './login.component.html'
})
export class LoginComponent {
    private user:User = new User('','');
    constructor(public loginServ :LoginService){}

    login(user: User) {
        this.loginServ.token(user).subscribe((val) => {
          if(val){
              console.log('logeado');
              console.log('User: ' + user.username);
              console.log('pass: ' + user.password);
              console.info(val);
          }
          else{
              console.info('credencial incorrecta');
          }
        });
      }
}

