import { Injectable }   from '@angular/core';
import { Observable }   from 'rxjs/Observable';
import * as _ from 'underscore';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/map';

@Injectable()
export class StatusService {
    public status: Array<any>;
    constructor(){
        var me:any = this
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
        if(me.status.length > 0){
            st = me.dropKeys(_.clone(me.status[0]))
            keys = Object.keys(st)
            keys.forEach(key => { pieJson[key] = 0; });
            me.status.forEach((s:any) => {
                keys.forEach(key => {
                    pieJson[key] =+ s[key]
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
        var stacked:Array<Array<any>> = []
        var keys: Array<any>
        var st: any
        if(me.status.length > 0){
            st = me.dropKeys(_.clone(me.status[0]))
            keys = Object.keys(st)
            keys.forEach(key => {
                stacked.push([key])
                me.status.forEach((status:any) => {
                    stacked[stacked.length-1].push(status[key])
                });
            });
        }
        return stacked
    }
    init = (ids: Array<any>):Observable<any>=>{
        this.status = [
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
        ]
        return Observable.of({status: this.status});
    }
    private dropKeys(status:any):any{
        delete status.id
        delete status._rev
        delete status.createdBy
        delete status.createdTime
        return status
    }
} 