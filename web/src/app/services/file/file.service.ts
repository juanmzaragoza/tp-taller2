import { Injectable }       from '@angular/core';
import { RemoteService }    from '../../services/remote/remote.service'
import { File }           from '../../models/file'


declare var _:any

@Injectable()
export class FileService {
    constructor(public RemoteServ: RemoteService){}
    get(){
       return this.RemoteServ.get('/files').map( res=> res.files)
    }
}
