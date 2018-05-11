import { Injectable }   from '@angular/core';
import { Observable }   from 'rxjs/Observable';

import 'rxjs/add/observable/of';

@Injectable()
export class StatusService {
    public request: Array<any>;
    constructor(){}
    
    getRequests = ()=>{
        var x = Math.floor((Math.random() * 10) + 1)
        this.request = [
            {frequency: 0, count: 0},
            {frequency: 1, count: 3},
            {frequency: 2, count: x}
        ]
        Observable.of(this.request);
    }
} 