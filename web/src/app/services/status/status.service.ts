import { Injectable }   from '@angular/core';
import { Observable }   from 'rxjs/Observable';
import { ServerService }    from '../server/server.service'
import { Status } from '../../models/status'
import { Server } from '../../models/server'
import { Stats } from '../../models/stats'
import { Request } from '../../models/request'
import * as _ from 'underscore';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/map';
import { Subscription } from 'rxjs/Subscription';
import {Observer} from 'rxjs/Observer';

declare var moment: any

@Injectable()
export class StatusService {
    private serversStatus: Array<Status>
    constructor(public ServerServ :ServerService){
        var me:any = this
        me.serversStatus = []
    }
    getDataPie = () : Array<Array<any>> => {
        var me:any = this
        var pie:Array<Array<any>> = []
        var pieJson :any = {}
        var keys: Array<any>
        var st: any
        var status = _.clone(me.serversStatus.map((ss:Status)=>{ return ss.stats}))
        if(status.length > 0){
            pie.push(["string", "number"])
            st = me.dropKeys(_.clone(status[0]))
            keys = Object.keys(st)
            keys.forEach(key => { pieJson[key] = 0; });
            status.forEach((s:Stats) => {
                keys.forEach(key => {
                    pieJson[key] = Number(pieJson[key]) + Number(s[key])
                });
            });
            keys.forEach(key => { 
                pie.push([key, pieJson[key]])
            });
        }
        return pie
    }
    getDataBarStacked = () : Array<Array<any>> => {
        var me:any = this
        var stacked:any = []
        var keys: Array<any>
        var st: any
        var status = _.clone(me.serversStatus.map((ss:Status)=>{ return ss.stats}))
        stacked.push(_.clone(me.serversStatus.map((ss:Status)=>{ return {type: "number", label:ss.name} })))
        stacked[0].unshift({type: 'string', label: 'Filters'})
        if(status.length > 0){
            st = me.dropKeys(_.clone(status[0]))
            keys = Object.keys(st)
            keys.forEach(key => {
                stacked.push([key])
                status.forEach((status:any) => {
                    stacked[stacked.length-1].push(Number(status[key]))
                });
            });
        }
        return stacked
    }
    getDataAreaStacked = () : Array<Array<any>> => {
        var me:any = this
        var area:Array<Array<any>> = []
        var keys: Array<any>
        var st: any
        var request = _.clone(me.serversStatus.map((ss:Status)=>{ return ss.request}))
        area.push(_.clone(me.serversStatus.map((ss:Status)=>{ return ss.name } )))
        area[0].unshift('Hour')
        if(request.length>0){
            for(var i=0; i<request[0].length; i++){
                area.push([request[0][i].hour])
            }
            request.forEach((r: Array<Request>) => {
                for(var i=0; i<r.length; i++){
                    area[i+1].push(r[i].count)
                }
            });
        }
        return area
    }
    ini = ():Observable<any>=>{
        var me:any = this
        me.serversStatus = []
        return Observable.create((observer: Observer<any>) => {
            me.ServerServ.getActive().subscribe(
                (servers:Array<Server>) =>{
                    var vec:Array<any> = []
                    if(servers.length >0){
                        /*eliminar este servidor dumy */
                        let servDumy = new Server()
                        servDumy = _.clone(servers[0])
                        servDumy.name = "dummy"
                        servers.push(servDumy)
                        /*eliminar este servidor dumy */
                        vec.push(me.loadStats(servers))
                        vec.push(me.loadRequest(servers))
                        return Observable.forkJoin(vec).subscribe(
                            (res:Array<any>)=>{
                                for(var i=0; i <servers.length;i++){
                                    me.serversStatus[i].stats = res[0][i]
                                    me.serversStatus[i].request = res[1][i]
                                }
                                observer.next({ok: true});
                            },
                            (console.error)
                        )
                    }
                    else{
                        return observer.next({ok: false});
                    }
                },
                (console.error)
            )
        }
        )
    }
    public get = ()=>{
        return this.serversStatus
    }
    private loadStats = (servers:Array<Server>) =>{
        var me:any = this
        let vec: Array<Observable<any>> = []
        servers.forEach((server:Server) => {
            let status = new Status(server.id, server.name)
            me.serversStatus.push(status)
            vec.push(me.ServerServ.stats(server.id))
        });
        return Observable.forkJoin(vec)
    }
    private loadRequest = (servers:Array<Server>) =>{
        var me:any = this,
        from = "00",
        to = moment().format("HH")
        let vec: Array<Observable<any>> = []
        servers.forEach((server:Server) => {
            vec.push(me.ServerServ.request(server.id, from, to))
        });
        return Observable.forkJoin(vec)
    }
    private dropKeys(status:any):any{
        delete status._id
        delete status.id
        delete status._rev
        delete status.createdBy
        delete status.createdTime
        return status
    }
} 