import { Component, 
    OnInit, 
    Input }        from '@angular/core'

declare var google:any

@Component({
    selector: 'app-chart',
    templateUrl: './chart.component.html'
})
export class ChartComponent {
    constructor(){}
    @Input() options: any;
    @Input() type: string;
    @Input() data: any;
    @Input() id: number;
    @Input() columns: Array<any>;

    ngOnInit() {
        var vm:any = this
        switch(this.type){
            case 'pie': 
            google.charts.load('current', {'packages':['corechart']});
            google.charts.setOnLoadCallback(()=>{
                var data = new google.visualization.DataTable();
                data.addColumn('string', 'Topping');
                data.addColumn('number', 'Slices');
                data.addRows(vm.data);
                var chart = new google.visualization.PieChart(document.getElementById('chart_div_'+ vm.id));
                chart.draw(data, vm.options);
            });
            break
            case 'barstacked':
                google.charts.load('current', {'packages':['corechart']});
                google.charts.setOnLoadCallback(()=>{
                    var data = new google.visualization.DataTable();
                    vm.columns.forEach((col:any) => {
                        data.addColumn(col.type, col.name);
                    });
                    data.addRows(vm.data);
                    var chart = new google.visualization.ColumnChart(document.getElementById('chart_div_'+ vm.id));
                    chart.draw(data, vm.options);
                })
            break
        }
    }

}