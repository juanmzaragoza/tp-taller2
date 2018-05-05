import { Component, 
         OnInit, 
         OnDestroy }        from '@angular/core'
import { ChartService }     from '../../services/common/chart.service'
import { StatusService }    from '../../services/status/status.service'
import { ServerService }    from '../../services/server/server.service'
import { Server }           from '../../models/server'

@Component({
    templateUrl: './status.component.html'
})
export class StatusComponent {
    private pid:number
    public title:string
    public servers: Array<Server>
    constructor(public ChartServ  :ChartService,
                public StatusServ :StatusService,
                public ServerServ :ServerService){}
    
    ngOnInit() {
        this.getServ()
        this.title = "Status"
    }
    ngOnDestroy() {
        var vm :any = this
        if (vm.pid) {
            clearInterval(vm.pid)
        }
    }
    getServ(){
        var me = this
        me.ServerServ.get().subscribe((res) => {
          me.servers = res.servers
        },
        error =>{
          console.error(error)
        });
    }
    change(id: any){

    }

}
