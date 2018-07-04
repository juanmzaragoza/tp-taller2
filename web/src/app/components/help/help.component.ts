import { Component, OnInit }    from '@angular/core'

@Component({
    templateUrl: './help.component.html'
})
export class HelpComponent {
    public title:string;
    public staff:Array<any>
    constructor(){
        this.staff = []
        this.staff.push({ name: 'Lucas Nicolas', lastname: 'Dominguez', mail:'nicolasdominguez7885@gmail.com', code: 97150 })
        this.staff.push({name: 'Kaouru',lastname: 'Haenna',mail:'kaoruheanna@gmail.com',code: 91891})
        this.staff.push({name: 'Viken',lastname: 'Masrian',mail:'masrianv@gmail.com',code: 96438})
        this.staff.push({name: 'Edward Erik',lastname: 'Schmoll',mail:'erikschmoll@gmail.com',code: 90135})
        this.staff.push({name: 'Juan Manuel',lastname: 'Zaragoza',mail:'juanmanuelzar@gmail.com',code: 92308})
    }
    
    ngOnInit() {
        this.title = 'Help'
      }
      open(){
          var man_url = 'https://drive.google.com/open?id=13PBGOVo2cbZI9ljhg8Eza9lnPf8tvyWa'
          window.open(man_url, "_blank");
      }
}