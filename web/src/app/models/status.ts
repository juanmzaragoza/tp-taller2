import {Stats} from '../models/stats'
export class Status{
    public id:string
    public name:string
    public stats:Stats
    constructor(_id?:any, _name?:string, stats?:Stats){
        this.id = _id? _id: '',
        this.name = _name? _name: '',
        this.stats = stats? stats : new Stats()
    }
}