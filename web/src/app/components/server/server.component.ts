import { Component, 
         OnInit, 
         EventEmitter }       from '@angular/core'
import { MaterializeDirective,
         MaterializeAction}   from "angular2-materialize";
import { Server }             from '../../models/server'
import { UserService }        from '../../services/user/user.service'
import { RemoteService }      from '../../services/remote/remote.service'
import { JsonService }      from '../../services/common/json.service'



declare var $ :any;

@Component({
    templateUrl: './server.component.html'
})
export class ServerComponent {
  title: string;
  servers: Array<any>;
  public server:Server = new Server();
  constructor(public UserServ: UserService,
              public RemoteServ: RemoteService,
              public JsonServ: JsonService){
      $('.modal').modal();
      this.servers = []
      this.title = 'Servers'
  }
  
  ngOnInit() {
    this.get()
  }

  open(){
    this.openModal();
      setTimeout(()=>{
          $('.btn-floating div').remove();
      }, 500);
  }
  get(){
    var me = this
    me.RemoteServ.get('/servers').subscribe((res) => {
      me.servers = res.servers
    },
    error =>{
      console.log(error)
    });
  }
  edit(serv: Server){
    var me = this
    me.server = serv
    console.log(serv)
    this.openModal();
  }
  delete(id:string){
    var me = this
    me.RemoteServ.delete('/servers/'+id).subscribe((res) => {
      me.servers = me.JsonServ.removeItem(me.servers, {id:id})
    },
    error =>{
      console.log(error)
    });
  }
  viewToken(token: string){
    console.info(token)
  }
  save(serv :Server){
    var me = this
    if(serv.id){
      console.log("edit")
      me.update(serv);
    }
    else{
      console.log("create")
      me.create(serv);
    }
  }
  update(serv: Server){
    var me = this
    me.RemoteServ.put('/servers/'+serv.id, serv).subscribe((res) => {
      me.servers = me.JsonServ.removeItem(me.servers, {id:serv.id})
      me.servers.push(res.server)
      me.server = new Server()
    },
    error =>{
      console.log(error)
    });
  }
  create(serv: Server){
    var me = this
    serv.createdTime = Date.now()
    serv.lastConnection = 0
    serv.createdBy = me.UserServ.getUser().username
    me.RemoteServ.post('/servers', serv).subscribe((res) => {
      me.servers.push(res.server)
      me.server = new Server()
    },
    error =>{
      console.log(error)
    });
  }
  modalActions = new EventEmitter<string|MaterializeAction>();
  openModal() {
    this.modalActions.emit({action:"modal",params:['open']});
  }
  closeModal() {
    this.modalActions.emit({action:"modal",params:['close']});
  } 
}
