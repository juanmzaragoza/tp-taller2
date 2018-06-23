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
        return Observable.of([
            new File("file 1", 30, "http://localhost/firebase/file-1.jpg", Date.now()),
            new File("file 2", 80, "http://localhost/firebase/file-2.jpg", Date.now()),
            new File("file 3", 22, "http://localhost/firebase/file-3.jpg", Date.now()),
            new File("file 4", 70, "http://localhost/firebase/file-4.jpg", Date.now()),
            new File("file 5", 14, "http://localhost/firebase/file-5.jpg", Date.now())
        ])
       //return this.RemoteServ.get('/files').map( res=> res.files)
    }
}
