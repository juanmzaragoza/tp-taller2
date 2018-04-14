import { Component, OnInit }    from '@angular/core'
import { SharedService } from '../../services/common/shared.service'
declare var $ :any;

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html'
})
export class NavBarComponent {
    role: string
    constructor(public SharedServ: SharedService){
        var me = this;
        me.role = 'default'
        this.SharedServ.startAccount.subscribe(
            (data: any) => {
                let jwtData = data.token.split('.')[1]
                let decodedJwtJsonData = window.atob(jwtData)
                let decodedJwtData = JSON.parse(decodedJwtJsonData)
                me.role = decodedJwtData.data.role
             });
    }
    
    ngOnInit() {
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
