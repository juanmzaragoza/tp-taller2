import { Component, OnInit }    from '@angular/core'
import { SharedService } from '../../services/common/shared.service'
import { UserService } from '../../services/user/user.service'
import { Config } from '../../config'
declare var $ :any;

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html'
})
export class NavBarComponent {
    role: string
    public tags: Array<any> =[]
    constructor(public SharedServ: SharedService, public UserServ: UserService){
        var me = this;
        me.role = 'default'
        this.SharedServ.startAccount.subscribe(
            (data: any) => { me.role = this.getRole(data.token); });
        this.SharedServ.logOff.subscribe( () => { me.role = 'default' });
        var t:string = this.UserServ.getToken();
        if(t){
            me.role = this.getRole(t);
        }
    }
    getRole(token:string){
        let jwtData = token.split('.')[1]
        let decodedJwtJsonData = window.atob(jwtData)
        let decodedJwtData = JSON.parse(decodedJwtJsonData)
        return decodedJwtData.data.role
    }
    
    ngOnInit() {
        this.tags = Config.web.tags
        $('.datepicker').pickadate({
            selectMonths: true, // Creates a dropdown to control month
            selectYears: 15 // Creates a dropdown of 15 years to control year
          });
          
          $('.button-collapse').sideNav({
                menuWidth: '80%', // Default is 240
                edge: 'left', // Choose the horizontal origin
                closeOnClick: true // Closes side-nav on <a> clicks, useful for Angular/Meteor
              }
            );
          
          $('select').material_select();
    }

}
