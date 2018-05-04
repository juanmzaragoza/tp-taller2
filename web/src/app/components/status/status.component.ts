import { Component, OnInit, OnDestroy }    from '@angular/core'

@Component({
    templateUrl: './status.component.html'
})
export class StatusComponent {
    private pid:number;
    constructor(){}
    
    ngOnInit() {
        
    
    }
    ngOnDestroy() {
        var vm :any = this
        if (vm.pid) {
            clearInterval(vm.pid);
        }
    }

}
