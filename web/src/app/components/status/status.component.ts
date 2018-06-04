import { Component, 
         OnInit, 
         OnDestroy }        from '@angular/core'
import { StatusService }    from '../../services/status/status.service'
import { Server }           from '../../models/server'

declare var google:any
declare var $:any
declare var moment: any
declare var _:any

@Component({
    templateUrl: './status.component.html'
})
export class StatusComponent {
    private pid:number
    public title: string
    public isServers:boolean

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
            if(vm.isServers){
                vm.drawAreaStacked();
                vm.drawPie();
                vm.drawBarStacked();
            }
        });
    }
    ngOnInit() {
        var vm: any = this
        vm.title = "Status"
        google.charts.load('current', {'packages':['corechart']});
        vm.draw()
    }
    ngOnDestroy() {
        var vm :any = this
        if (vm.pid) {
            clearInterval(vm.pid)
        }
    }
    update(){
        var vm :any = this,
        newHour:any = {},
        from = moment().format("HH"),
        to = from
        vm.StatusServ.update(from, to).subscribe(
            ((res:any)=>{
                if(res.ok){
                    var data = vm.StatusServ.getDataAreaStacked();
                    var index = vm.graph.area.data.getNumberOfRows()-1
                    var lastHour = vm.graph.area.data.getValue(index,0)
                    if(data[1][0] == lastHour){
                        vm.graph.area.dataArea.splice(vm.graph.area.dataArea.length -1, 1);
                        vm.graph.area.dataArea.push(data[1])
                        vm.graph.area.data.removeRow(index);
                        vm.graph.area.data.insertRows(index, [data[1]]);
                    }
                    else{
                        vm.graph.area.dataArea.push(data[1])
                        vm.graph.area.data.insertRows(index, [data[1]]);
                    }
                    vm.graph.area.chart.draw(vm.graph.area.data, vm.graph.area.options);
                    
                }
            }),
            (console.error)
        )
        /*
        
       
        if(newHour[0] == lastHour){
            vm.graph.area.data.removeRow(vm.graph.area.data.getNumberOfRows()-1);
        }
        vm.graph.area.data.insertRows(vm.graph.area.data.getNumberOfRows()-1, [newHour]);
        vm.graph.area.chart.draw(vm.graph.area.data, vm.graph.area.options);*/
        /*console.info(vm.graph.area.data.getValue(vm.graph.area.data.getNumberOfRows()-1,0))
        console.info(vm.graph.area.data.getValue(vm.graph.area.data.getNumberOfRows()-1,1))
        console.info(vm.graph.area.data.getValue(vm.graph.area.data.getNumberOfRows()-1,2))*/
    }
    draw(){
        var vm :any = this
        this.StatusServ.ini().subscribe(
            (res=>{
                vm.isServers = res.ok
                if(vm.isServers){
                    vm.graph.area.dataArea = _.clone(vm.StatusServ.getDataAreaStacked());
                    vm.drawAreaStacked();
                    vm.drawPie();
                    vm.drawBarStacked();
                    vm.pid = setInterval(() => {
                        vm.update(); 
                    }, 10000);
                }
            }),
            (console.error)
        )
    }

    drawPie(){
        var vm = this
        google.charts.setOnLoadCallback(()=>{
            var data = vm.StatusServ.getDataPie()
            vm.graph.pie.data = google.visualization.arrayToDataTable(data);
            vm.graph.pie.chart = new google.visualization.PieChart(document.getElementById('chart_pie'));
            vm.graph.pie.chart.draw(vm.graph.pie.data, vm.graph.pie.options);
        });
    }
    drawBarStacked(){
        var vm = this
        google.charts.setOnLoadCallback(()=>{
            var data = vm.StatusServ.getDataBarStacked()
            vm.graph.bar.data = google.visualization.arrayToDataTable(data);
            vm.graph.bar.chart = new google.visualization.ColumnChart(document.getElementById('chart_barStacked'));
            vm.graph.bar.chart.draw(vm.graph.bar.data, vm.graph.bar.options);
        })
    }
    drawAreaStacked(){
        var vm = this
        google.charts.setOnLoadCallback(()=>{
            vm.graph.area.data = google.visualization.arrayToDataTable(vm.graph.area.dataArea);
            vm.graph.area.chart = new google.visualization.AreaChart(document.getElementById('area'));
            vm.graph.area.chart.draw(vm.graph.area.data, vm.graph.area.options);
        })
    }

}
