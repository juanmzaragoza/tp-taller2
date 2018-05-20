import { Injectable }   from '@angular/core';
import { Observable }   from 'rxjs/Observable';
import { ServerService }    from '../server/server.service'
import { Status } from '../../models/status'
import { Server } from '../../models/server'
import { Stats } from '../../models/stats'
import * as _ from 'underscore';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/map';
import { Subscription } from 'rxjs/Subscription';
import {Observer} from 'rxjs/Observer';

@Injectable()
export class StatusService {
    //public status: Array<any>;
    //private servers: Array<any>;
    private serversStatus: Array<Status>
    constructor(public ServerServ :ServerService){
        var me:any = this
        me.serversStatus = []
        //me.servers = []
        /*me.getStatus().subscribe(
            (res:any) =>{
                me.status = res.status
            },
            (error:any) =>{
              console.error(error)
            })*/
    }
    getDataPie = () : Array<Array<any>> => {
        var me:any = this
        var pie:Array<Array<any>> = []
        var pieJson :any = {}
        var keys: Array<any>
        var st: any
        var status = _.clone(me.serversStatus.map((ss:Status)=>{ return ss.stats}))
        if(status.length > 0){
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
        var stacked:any = {}//Array<Array<any>> = []
        var keys: Array<any>
        var st: any
        var status = _.clone(me.serversStatus.map((ss:Status)=>{ return ss.stats}))
        stacked["data"] = []
        stacked["columns"] = _.clone(me.serversStatus.map((ss:Status)=>{ return {type: "number", name:ss.name} }))
        if(status.length > 0){
            st = me.dropKeys(_.clone(status[0]))
            keys = Object.keys(st)
            keys.forEach(key => {
                stacked.data.push([key])
                status.forEach((status:any) => {
                    stacked.data[stacked.data.length-1].push(Number(status[key]))
                });
            });
        }
        return stacked
    }
    ini = ():Observable<any>=>{
        var me:any = this
        console.log("paso 1, voy a buscar los servidores")
        me.serversStatus = []
        return Observable.create((observer: Observer<any>) => {
            me.ServerServ.getActive().subscribe(
                (servers:Array<Server>) =>{
                    console.log("paso 2, servers:", servers)
                    me.getStats(servers).subscribe(
                        (serversStats:Array<Stats>)=>{
                            for(var i=0; i <servers.length;i++){
                                me.serversStatus[i].stats = serversStats[i]
                            }
                            console.log("paso 7, finalizo:", me.serversStatus)
                            //return Observable.of({ok: true})
                            observer.next({ok: true});
                        },
                        (error:any) =>{
                            console.log("paso 7 bis, error:", error)
                            //return Observable.of({ok: false})
                            //return {ok: false}
                            observer.next({ok: false});
                        }
                    )
                },
                (error:any) =>{
                    //return Observable.of({ok: false})
                    //return {ok: false}
                    observer.next({ok: false});
                }
            )
        }
        )
/*
        return Observable.of(me.ServerServ.getActive().subscribe(
            (servers:Array<Server>) =>{
                console.log("paso 2, servers:", servers)
                me.getStats(servers).subscribe(
                    (serversStats:Array<Stats>)=>{
                        for(var i=1; i <servers.length;i++){
                            me.serversStatus[i].stats = serversStats[i]
                        }
                        console.log("paso 7, finalizo:", me.serversStatus)
                        //return Observable.of({ok: true})
                        return {ok: true}
                        
                    },
                    (error:any) =>{
                        console.log("paso 7 bis, error:", error)
                        //return Observable.of({ok: false})
                        return {ok: false}
                    }
                )
            },
            (error:any) =>{
                //return Observable.of({ok: false})
                return {ok: false}
            }
        )
        )*/
    }
    public get = ()=>{
        return this.serversStatus
    }
    private getStats = (servers:Array<Server>) =>{
        var me:any = this
        let vec: Array<Observable<any>> = []
        /*eliminar este servidor dumy */
        let servDumy = new Server()
        servDumy = _.clone(servers[0])
        servDumy.name = "dumy"
        servers.push(servDumy)
        /*eliminar este servidor dumy */
        console.log("paso 3, agrego dumy:", servers)
        servers.forEach((server:Server) => {
            let status = new Status(server.id, server.name)
            me.serversStatus.push(status)
            console.log("paso 4, creo un status:", status)
            vec.push(me.ServerServ.stats(server.id)
            
            /*.subscribe(
                (stats:Stats) =>{
                    status.stats = stats
                    console.log("paso 5, recibo su stats:", status)
                    me.serversStatus.push(status)
                    console.log("paso 6, lo gaurdo:", me.serversStatus)
                },
                (error:any) =>{
                  console.error(error)
                })*/
            )
        });
        return Observable.forkJoin(vec)
    }
    init = (ids: Array<any>):Observable<any>=>{
        /*this.status = [
            {
                "id": "1",
                "_rev": "string",
                "createdBy": "string",
                "createdTime": 0,
                "numUsers": 10,
                "numUsersActiveToday": 4,
                "numStoriesToday": 60,
                "numFastStoriesToday": 40,
                "numStories": 100,
                "numUsersMessages": 3060,
                "numUsersMessagesToday": 1000,
                "numAcceptedContactsToday": 3
            },
            {
                "id": "2",
                "_rev": "string",
                "createdBy": "string",
                "createdTime": 0,
                "numUsers": 60,
                "numUsersActiveToday": 40,
                "numStoriesToday": 260,
                "numFastStoriesToday": 70,
                "numStories": 180,
                "numUsersMessages": 9070,
                "numUsersMessagesToday": 1900,
                "numAcceptedContactsToday": 29
            }
        ]*/
        return Observable.of({status: true});
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