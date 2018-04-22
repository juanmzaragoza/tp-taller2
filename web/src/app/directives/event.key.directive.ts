import { Directive, Input, HostListener, OnInit, ElementRef, Renderer } from '@angular/core'

@Directive({
  selector: '[eventClickbyKey]',
})
export class EventKeyDirective implements OnInit {
    @Input() eventClickbyKey: number;
    key:number;
    constructor(public elem: ElementRef, public renderer:Renderer){}

    ngOnInit(){
        this.key = this.eventClickbyKey
    }

    @HostListener('window:keyup', ['$event'])
    keyEvent(event: KeyboardEvent) {
        if (event.keyCode == this.key) {
            let event = new MouseEvent('click', {bubbles: true});
            this.renderer.invokeElementMethod(
                this.elem.nativeElement, 'dispatchEvent', [event]);
        }
    }
}
