import { Injectable }       from '@angular/core';
import { RemoteService }    from '../../services/remote/remote.service'
import { Server }           from '../../models/server'
import { Request }           from '../../models/request'
import { Observable }       from 'rxjs/Observable';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/map';
import { Subscription } from 'rxjs/Subscription';
import {Observer} from 'rxjs/Observer';

declare var _:any

@Injectable()
export class ServerService {
    constructor(public RemoteServ: RemoteService){}
    get(){
       return this.RemoteServ.get('/servers')
       .map( res=> this.mapperList(res.servers))
    }
    getActive = ():Observable<any> =>{
      var me:any = this
      var serv :Array<Server>= []
      return Observable.create((observer: Observer<any>) => {
        me.get().subscribe((servers: Array<Server>) =>{
          if(servers.length >0){
            me.getPingAll(servers).subscribe(
              (pings:Array<any>)=>{
                  for(var i=0; i <pings.length;i++){
                      if(pings[i].ok){
                        serv.push(servers.filter(s =>{return (s.id == pings[i].id)})[0])
                      }
                  }
                  observer.next(serv);
              },
              (console.error)
            )
          }
          else{ return observer.next([]); }
          },
          (console.error)
        )
      })
    }
    getPingAll=(servers: Array<Server>)=>{
      var me:any = this
      let vec: Array<Observable<any>> = []
      servers.forEach((server:Server) => {
          vec.push(me.ping2(server.id))
      });
      return Observable.forkJoin(vec)
    }
    delete(id:string){
      return this.RemoteServ.delete('/servers/'+id);
    }
    refreshToken(id:string){
      return this.RemoteServ.post('/servers/'+id,{id:id});
    }
    update(serv: Server){
      return this.RemoteServ.put('/servers/'+serv.id, serv);
    }
    create(serv: Server){
      return this.RemoteServ.post('/servers', serv)
      .map( res=> this.mapper(res.server));
    }
    ping(id: string){
      return this.RemoteServ.get('/servers/ping/'+ id).map(res => res.ping);
    }
    ping2=(id: string)=>{
      var me:any = this
      //return this.RemoteServ.get('/ping/'+ id).map(res => res.server);
      return Observable.create((observer: Observer<any>) => {
        me.ping(id).subscribe(
          (server:any)=>{
            observer.next({ok: true, id: id});
            observer.complete()
          },
          (err:any)=>{
            observer.next({ok: false, id: id});
            observer.complete()
          }
        )
      })
    }
    stats(id: string){
      return this.RemoteServ.get('/servers/stats/'+ id).map(res => res.stats);
    }
    request(id:string , from:string, to:string){
      var req:Array<Request> = []
      var hours:Array<String> = [
        '00','01','02','03','04','05',
        '06','07','08','09','10','11',
        '12','13','14','15','16','17',
        '18','19','20','21','22','23']
      var max = 23, min = 0
      var res:Array<Request> = []
      hours.forEach((hour:string) => {
          var count = Math.floor(Math.random() * (max - min + 1)) + min
          req.push(new Request(hour, count))
      });
      var nfrom:number = Number(from)
      var nto:number = Number(to)
      req.forEach(r => {
        var nhour:number = Number(r.hour)
        if(nhour>=nfrom && nhour<=nto){
          res.push(_.clone(r))
        }
      });
      return Observable.of(res)
    }
    mapper(obj:any){
      var serv = _.clone(obj.server)
      serv["token"] = _.clone(obj.token)
      return serv
    }
    mapperList(res:Array<any>){
      var servs:Array<Server> = []
      res.forEach(item => {
        servs.push(this.mapper(item))
      });
      return servs
    }
} 