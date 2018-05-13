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
                            type: "pie",
                            options: {
                                'title':'Total',
                                'width':600,
                                'height':300
                            },
                            data:[]
                        }
                    }
                    
                }
    
    ngOnInit() {
        var vm: any = this
        this.getServ()
        this.title = "Status"
        this.chart["pie"]["data"] = vm.StatusServ.getDataPie()
        /*
         // Load the Visualization API and the corechart package.
      google.charts.load('current', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.charts.setOnLoadCallback(drawChart);
        */
      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Topping');
        data.addColumn('number', 'Slices');
        data.addRows(vm.StatusServ.getDataPie());

        // Set chart options
        var options = {'title':'Total',
                       'width':600,
                       'height':300};

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data, options);
        
      }

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
        },
        error =>{
          console.error(error)
        });
    }
    change(id: any){

    }

}
