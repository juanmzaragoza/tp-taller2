
export class User{
    token: string;
    constructor(public username?: string, public password?: string){}
    setToken = (t:string)=>{
        this.token = t;
    }
    getToken = ()=>{
        return this.token;
    }
}
