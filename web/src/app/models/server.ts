
export class Server{
    id: string
    name: string
    host: string
    port: string
    pingUrl: string
    createdTime: number
    createdBy: string
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
        this.createdBy = ''
        this.lastConnection = 0
    }
}