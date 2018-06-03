import { Injectable }       from '@angular/core';
import { RemoteService }    from '../../services/remote/remote.service'
import { Server }           from '../../models/server'
import { Observable }       from 'rxjs/Observable';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/map';
import { Subscription } from 'rxjs/Subscription';
import {Observer} from 'rxjs/Observer';

@Injectable()
export class ServerService {
    constructor(public RemoteServ: RemoteService){}
    get(){
       return this.RemoteServ.get('/servers')
       .map( res=> res.servers)
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
      return this.RemoteServ.post('/servers', serv);
    }
    ping(id: string){
      return this.RemoteServ.get('/ping/'+ id).map(res => res.ping);
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
      return this.RemoteServ.get('/stats/'+ id).map(res => res.stats);
    }
} 