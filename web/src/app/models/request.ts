export class Request{
    public hour:string
    public count:number
    constructor(_hour?:string, _count?:number){
        this.hour = _hour? _hour: '',
        this.count = _count? _count : 0
    }
}