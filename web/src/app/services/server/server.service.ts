import { Injectable }       from '@angular/core';
import { RemoteService }    from '../../services/remote/remote.service'
import { Server }           from '../../models/server'
import { Observable }       from 'rxjs/Observable';

@Injectable()
export class ServerService {
    constructor(public RemoteServ: RemoteService){}
    get(){
        return this.RemoteServ.get('/servers');
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
} 