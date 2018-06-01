import { Component, 
         OnInit, 
         OnDestroy }        from '@angular/core'
import { ChartService }     from '../../services/common/chart.service'
import { StatusService }    from '../../services/status/status.service'
import { ServerService }    from '../../services/server/server.service'
import { Server }           from '../../models/server'

declare var google:any
declare var $:any
@Component({
    templateUrl: './status.component.html'
})
export class StatusComponent {
    private pid:number
    public title:string
    public titleStacked: string
    public servers: Array<Server>
    public chart: any

    private graph: any = {}
    constructor(public ChartServ  :ChartService,
                public StatusServ :StatusService,
                public ServerServ :ServerService){
                    this.graph.area = {}
                    this.graph.pie = {}
                    this.graph.bar = {}
                    this.graph.area.options = {
                        'title':'Total of each server',
                        'width':600,
                        'height':300,
                        'isStacked': true,
                        'legend': {position: 'top', maxLines: 3}
                    }
                    this.graph.pie.options = {
                        'title':'Total',
                        'width':600,
                        'height':300
                    }
                    this.graph.bar.options = {
                        'title':'Total of each server',
                        'width':600,
                        'height':300,
                        'isStacked': true,
                        'legend': {position: 'top', maxLines: 3},
                        'vAxis': {minValue: 0}
                    }


                    this.titleStacked = "bar stacked"
                    this.chart = {
                        barStacked:{
                            columns:[],
                            options: {
                                'title':'Total of each server',
                                'width':600,
                                'height':300,
                                'isStacked': true,
                                'legend': {position: 'top', maxLines: 3},
                            },
                            data:[]
                        },
                        areaStacked:{
                            columns:[],
                            options: {
                                'title':'Total of each server',
                                'width':600,
                                'height':300,
                                'isStacked': true,
                                'legend': {position: 'top', maxLines: 3},
                                'vAxis': {minValue: 0}
                            },
                            data:[]
                        }
                    }
                }
    
    ngOnInit() {
        var vm: any = this
        this.title = "Status"
        google.charts.load('current', {'packages':['corechart']});
        this.draw()
    }
    ngOnDestroy() {
        var vm :any = this
        if (vm.pid) {
            clearInterval(vm.pid)
        }
    }
    change(id: any){
        var vm :any = this
        vm.graph.area.data.removeRow(0);
        vm.graph.area.data.insertRows(vm.graph.area.count - 1, [['2017', 2030, 1040]]);
        vm.graph.area.chart.draw(vm.graph.area.data)
    }
    draw(){
        var vm :any = this
        vm.drawAreaStacked()
        this.StatusServ.ini().subscribe(
            (ok=>{
                vm.drawPie()
                /*this.chart["pie"]["data"] = vm.StatusServ.getDataPie()
                var stacked = vm.StatusServ.getDataBarStacked()
                this.chart["barStacked"]["columns"] = stacked.columns
                this.chart["barStacked"]["columns"].unshift({type: 'string', name: 'Filters'})
                this.chart["barStacked"]["data"] = stacked.data
                vm.drawBarStacked()*/
            }),
            (console.info)
        )
    }

    drawPie(){
        var vm = this
        google.charts.setOnLoadCallback(()=>{
            var data = vm.StatusServ.getDataPie()
            vm.graph.pie.count = data.length
            console.info(data)
            vm.graph.pie.data = google.visualization.arrayToDataTable(data);
            vm.graph.pie.chart = new google.visualization.PieChart(document.getElementById('chart_pie'));
            vm.graph.pie.chart.draw(vm.graph.pie.data, vm.graph.pie.options);
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
    drawAreaStacked(){
        var vm = this
        google.charts.setOnLoadCallback(()=>{
            vm.graph.area.data = google.visualization.arrayToDataTable([
                ['Year', 'Sales', 'Expenses'],
                ['2013',  1000,      400],
                ['2014',  1170,      460],
                ['2015',  660,       1120],
                ['2016',  1030,      540]
              ]);
              vm.graph.area.count = 4
            vm.graph.area.chart = new google.visualization.AreaChart(document.getElementById('area'));
            vm.graph.area.chart.draw(vm.graph.area.data, vm.graph.area.options);
        })
    }

}
