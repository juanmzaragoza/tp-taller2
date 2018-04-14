
export class Server{
    name: string
    host: string
    port: string
    pingUrl: string
    createdTime: number
    createdby: string
    lastConnection: number
    constructor(){
        this.clean()
    }
    clean(){
        this.host = ''
        this.port = ''
        this.name = ''
        this.pingUrl = ''
        this.createdTime = 0
        this.createdby = ''
        this.lastConnection = 0
    }
}