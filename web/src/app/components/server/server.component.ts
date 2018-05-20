import { Component, 
         OnInit, 
         EventEmitter,
        OnDestroy }       from '@angular/core'
import { MaterializeDirective,
         MaterializeAction,
         toast}               from "angular2-materialize";

import { Server }             from '../../models/server'
import { UserService }        from '../../services/user/user.service'
import { ServerService }      from '../../services/server/server.service'
import { JsonService }        from '../../services/common/json.service'
import { ClipBoardService }   from '../../services/common/clipboard.service'



declare var $ :any;
declare var M_Modal : any;

@Component({
    templateUrl: './server.component.html'
})
export class ServerComponent {
  title: string;
  servers: Array<any>;
  pid: any;
  public server:Server = new Server();
  constructor(public UserServ: UserService,
              public ServerServ: ServerService,
              public JsonServ: JsonService,
              public ClipBoardServ: ClipBoardService){
      this.servers = []
      this.title = 'Servers'
  }
  
  ngOnInit() {
    var vm = this
    vm.get()
    vm.pid = setInterval(() => {
      vm.ping(); 
    }, 10000);
  }
  ngOnDestroy() {
    if (this.pid) {
      clearInterval(this.pid);
    }
  }
  ping(){
    var vm = this
    if(vm.servers.length>0){
      vm.servers.forEach(s => {
        vm.ServerServ.ping(s.id).subscribe((ping) => {
          s["active"] = ping.status
        },
        error =>{
          s["active"] = "none"
        });
      });
    }
  }
  open(){
    this.openModal();
    $('.modal').modal();
      setTimeout(()=>{
        $('input[name=name]').focus()
      }, 1000);
  }
  get(){
    var me = this
    me.ServerServ.get().subscribe((servers) => {
      console.log(servers)
      me.servers = servers
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
    me.ServerServ.delete(id).subscribe((res) => {
      me.servers = me.JsonServ.removeItem(me.servers, {id:id})
      toast("the server was deleted",4000)
    },
    error =>{
      console.log(error)
    });
  }
  copyToken(token: any){
    this.ClipBoardServ.copy(token.token)
    toast("copy to clipboard",4000)
  }
  refreshToken(id:string){
    var me = this
    me.ServerServ.refreshToken(id).subscribe((res) => {
      me.servers = me.JsonServ.removeItem(me.servers, {id:id})
      me.servers.push(res.server)
      toast("the token was updated",4000)
    },
    error =>{
      console.log(error)
    });
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
    me.ServerServ.update(serv).subscribe((res) => {
      me.servers = me.JsonServ.removeItem(me.servers, {id:serv.id})
      me.servers.push(res.server)
      me.server = new Server()
      toast("the server was updated",4000)
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
    me.ServerServ.create(serv).subscribe((res) => {
      res.server["active"] = "none"
      me.servers.push(res.server)
      me.server = new Server()
      toast("the server was created",4000)
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
    var me = this
    me.server = new Server()
    this.modalActions.emit({action:"modal",params:['close']});
  } 
}
