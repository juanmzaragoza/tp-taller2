
export class Server{
    id: string
    name: string
    host: string
    createdTime: number
    createdBy: string
    lastConnection: number
    constructor(){
        this.clean()
    }
    clean(){
        this.host = ''
        this.name = ''
        this.createdTime = undefined
        this.createdBy = ''
    }
}