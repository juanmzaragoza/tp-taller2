import { Directive, HostListener, ElementRef } from '@angular/core'

declare var $ :any;
@Directive({
  selector: '[FixButtonDirective]',
})
export class FixButtonDirective {
    constructor(public elem: ElementRef){}
    @HostListener('click', ['$event']) onClick($event: any){
        setTimeout(()=>{
            $('.btn-floating div').remove();
            $('a div').remove();
        }, 500);
    }
}
