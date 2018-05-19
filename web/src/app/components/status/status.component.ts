import { Component, 
         OnInit, 
         OnDestroy }        from '@angular/core'
import { ChartService }     from '../../services/common/chart.service'
import { StatusService }    from '../../services/status/status.service'
import { ServerService }    from '../../services/server/server.service'
import { Server }           from '../../models/server'

declare var google:any

@Component({
    templateUrl: './status.component.html'
})
export class StatusComponent {
    private pid:number
    public title:string
    public titleStacked: string
    public servers: Array<Server>
    public chart: any
    constructor(public ChartServ  :ChartService,
                public StatusServ :StatusService,
                public ServerServ :ServerService){
                    this.titleStacked = "bar stacked"
                    this.chart = {
                        pie:{
                            id: 1,
                            type: "pie",
                            columns:[],
                            options: {
                                'title':'Total',
                                'width':600,
                                'height':300
                            },
                            data:[]
                        },
                        barStacked:{
                            id:2,
                            type: "barstacked",
                            columns:[],
                            options: {
                                'title':'Total of each server',
                                'width':600,
                                'height':300,
                                'isStacked': true,
                                'legend': {position: 'top', maxLines: 3},
                            },
                            data:[]
                        }
                    }
                    
                }
    
    ngOnInit() {
        var vm: any = this
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
          this.chart["pie"]["data"] = me.StatusServ.getDataPie()
          this.chart["barStacked"]["columns"] = res.servers.map((s:any)=>{return {type: 'number', name: s.name} })
          this.chart["barStacked"]["columns"].unshift({type: 'string', name: 'Filters'})
          this.chart["barStacked"]["data"] = me.StatusServ.getDataBarStacked()
          me.drawPie()
          me.drawBarStacked()
        },
        error =>{
          console.error(error)
        });
    }
    change(id: any){

    }

    drawPie(){
        var me = this
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(()=>{
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Topping');
            data.addColumn('number', 'Slices');
            data.addRows(me.chart.pie.data);
            var chart = new google.visualization.PieChart(document.getElementById('chart_pie'));
            chart.draw(data, me.chart.pie.options);
        });
    }
    drawBarStacked(){
        var me = this
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(()=>{
            var data = new google.visualization.DataTable();
            me.chart.barStacked.columns.forEach((col:any) => {
                data.addColumn(col.type, col.name);
            });
            data.addRows(me.chart.barStacked.data);
            var chart = new google.visualization.ColumnChart(document.getElementById('chart_barStacked'));
            chart.draw(data, me.chart.barStacked.options);
        })
    }

}
