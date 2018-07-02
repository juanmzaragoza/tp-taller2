import { Component, EventEmitter }    from '@angular/core';
import { Router }       from '@angular/router';
import { LoginService } from '../../services/login/login.service'
import { User }         from '../../models/user'
import { Observable }   from 'rxjs/Observable';
import { Location }     from '@angular/common';
import { SharedService } from '../../services/common/shared.service'
import {MaterializeDirective,MaterializeAction} from "angular2-materialize";
declare var $ :any;

@Component({
    templateUrl: './login.component.html'
})
export class LoginComponent {
    public user:User = new User('','');
    public message: string
    public globalActions = new EventEmitter<string|MaterializeAction>();
    constructor(public loginServ :LoginService, 
                public RouterServ : Router,
                public Location: Location,
                public SharedServ: SharedService){
                  var me = this
                  me.message = ''
                }
    ngAfterViewInit() {
      $('input[name=name]').focus()
    }
    login(user: User) {
        this.loginServ.token(user).subscribe((val) => {
          if(val){
                this.message = ""
                this.SharedServ.startAccount.emit(user);
                this.RouterServ.navigate(['/home']);
          }
          else{
            //this.message = "wrong user or password"
            this.globalActions.emit('toast')
          }
        },
      error =>{
        //this.message = "wrong user or password"
        this.globalActions.emit('toast')
      });
      }
      back(){
        //this.Location.back();
        this.RouterServ.navigate(['/home']);
      }
}

