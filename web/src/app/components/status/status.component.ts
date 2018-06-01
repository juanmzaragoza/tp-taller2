import { Component, 
         OnInit, 
         OnDestroy }        from '@angular/core'
import { StatusService }    from '../../services/status/status.service'
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

    private graph: any = {}
    constructor(public StatusServ :StatusService){
        var vm: any = this
        vm.graph.area = {}
        vm.graph.pie = {}
        vm.graph.bar = {}
        vm.graph.area.options = {
            title:'Total requests of each server',
            isStacked: true,
            legend: {position: 'top', maxLines: 3}
        }
        vm.graph.pie.options = {
            title:'Total of all servers'
        }
        vm.graph.bar.options = {
            title:'Total of each server',
            isStacked: true,
            legend: {position: 'top', maxLines: 3},
            vAxis: {minValue: 0}
        }
        $(window).resize(function(){
            vm.drawAreaStacked();
            vm.drawPie();
            vm.drawBarStacked();
        });
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
        this.StatusServ.ini().subscribe(
            (ok=>{
                google.charts.setOnLoadCallback(vm.drawAreaStacked());
                google.charts.setOnLoadCallback(vm.drawPie());
                google.charts.setOnLoadCallback(vm.drawBarStacked());
            }),
            (console.info)
        )
    }

    drawPie(){
        var vm = this
        google.charts.setOnLoadCallback(()=>{
            var data = vm.StatusServ.getDataPie()
            vm.graph.pie.count = data.length
            vm.graph.pie.data = google.visualization.arrayToDataTable(data);
            vm.graph.pie.chart = new google.visualization.PieChart(document.getElementById('chart_pie'));
            vm.graph.pie.chart.draw(vm.graph.pie.data, vm.graph.pie.options);
        });
    }
    drawBarStacked(){
        var vm = this
        google.charts.setOnLoadCallback(()=>{
            var data = vm.StatusServ.getDataBarStacked()
            vm.graph.bar.count = data.length
            vm.graph.bar.data = google.visualization.arrayToDataTable(data);
            vm.graph.bar.chart = new google.visualization.ColumnChart(document.getElementById('chart_barStacked'));
            vm.graph.bar.chart.draw(vm.graph.bar.data, vm.graph.bar.options);
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
