import { Component, 
         OnInit, 
         EventEmitter }       from '@angular/core'
import { MaterializeDirective,
         MaterializeAction}   from "angular2-materialize";
import { Server }             from '../../models/server'
import { UserService }        from '../../services/user/user.service'
import { RemoteService }      from '../../services/remote/remote.service'



declare var $ :any;

@Component({
    templateUrl: './server.component.html'
})
export class ServerComponent {
  private server:Server = new Server();
  constructor(public UserServ: UserService,
              public RemoteServ: RemoteService){
      $('.modal').modal();
  }
  
  ngOnInit() {

  }

  open(){
    this.openModal();
      setTimeout(()=>{
          $('.btn-floating div').remove();
      }, 500);
  }
  save(serv :Server){
    var me = this
    serv.createdTime = Date.now()
    serv.lastConnection = 0
    serv.createdby = me.UserServ.getUser().username
    console.log(serv)
    me.RemoteServ.post('/servers', serv).subscribe((res) => {
      console.log(res)
    },
    error =>{
      console.log(error)
    });
    //me.server.clean()
  }
  modalActions = new EventEmitter<string|MaterializeAction>();
  openModal() {
    this.modalActions.emit({action:"modal",params:['open']});
  }
  closeModal() {
    this.modalActions.emit({action:"modal",params:['close']});
  } 
}



/* 

modalActions1 = new EventEmitter<string|MaterializeAction>();
        modalActions2 = new EventEmitter<string|MaterializeAction>();
        globalActions = new EventEmitter<string|MaterializeAction>();
        params:Array<any> = []
      
        model1Params = [
          {
            dismissible: false,
            complete: function() { console.log('Closed'); }
          }
        ]
      
        printSomething() {
          console.log("tooltip button clicked!");
        }
        triggerToast() {
          this.globalActions.emit('toast')
        }
        openModal1() {
          this.modalActions1.emit({action:"modal",params:['open']});
        }
        closeModal1() {
          this.modalActions1.emit({action:"modal",params:['close']});
        }
        openModal2() {
          this.modalActions2.emit({action:"modal",params:['open']});
        }
        closeModal2() {
          this.modalActions2.emit({action:"modal",params:['close']});
        }
*/