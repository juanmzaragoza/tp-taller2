import {Stats} from '../models/stats'
import {Request} from '../models/request'
export class Status{
    public id:string
    public name:string
    public stats:Stats
    public request: Array<Request>
    constructor(_id?:any, _name?:string, stats?:Stats, _request?:Array<Request>){
        this.id = _id? _id: '',
        this.name = _name? _name: '',
        this.stats = stats? stats : new Stats()
        this.request = _request? _request : new Array<Request>()
    }
}