import { Component, OnInit, OnDestroy }    from '@angular/core'

declare var d3:any;

@Component({
    templateUrl: './status.component.html'
})
export class StatusComponent {
    private pid:number;
    constructor(){}
    
    ngOnInit() {
        console.log(d3)
    }
    ngOnDestroy() {
        var vm :any = this
        if (vm.pid) {
            clearInterval(vm.pid)
        }
    }

}
