import { Component, 
         OnInit, 
         OnDestroy }        from '@angular/core'
import { ChartService }     from '../../services/common/chart.service'
import { StatusService }    from '../../services/status/status.service'
import { RemoteService }    from '../../services/remote/remote.service'


@Component({
    templateUrl: './status.component.html'
})
export class StatusComponent {
    private pid:number;
    constructor(public ChartServ  :ChartService,
                public StatusServ :StatusService,
                public RemoteServ :RemoteService){}
    
    ngOnInit() {
        
    }
    ngOnDestroy() {
        var vm :any = this
        if (vm.pid) {
            clearInterval(vm.pid)
        }
    }

}
