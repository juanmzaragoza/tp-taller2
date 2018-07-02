import { Injectable }       from '@angular/core';
import { RemoteService }    from '../../services/remote/remote.service'
import { File }           from '../../models/file'


/*
    TODO: quitar los import de abajo, se usaron para el mock
*/
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/map';


declare var _:any

@Injectable()
export class FileService {
    constructor(public RemoteServ: RemoteService){}
    get = (): Observable<Array<File>>=>{
       return this.RemoteServ.get('/files').map( res=> res.files)
    }
    createbyFirebase = (file: File): Observable<File> =>{
        var body = {
            "file": file.resource,
            "metadata": {
                id: file.id,
                _rev: file._rev
            }
        }
        return this.RemoteServ.post('/files/upload', body)
       .map(res=> res.file);
    }
    create = (file: File): Observable<File> =>{
        file.resource = ''
        return this.RemoteServ.post('/files', file)
      .map( res=> res.file);
        //return Observable.of(file)
    }
    delete(id:string){
      return this.RemoteServ.delete('/files/'+id);
    }
}
