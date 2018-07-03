
export class File{
    public id: string
    public _rev: string
    public createdTime: number
    public updatedTime: number
    public size: number
    public filename: string
    public resource: string
    constructor(filename?:string, size?:number, resource?: string, createdTime?: number, updatedTime?: number){
        this.filename = filename
        this.size = size
        this.resource = resource
        this.createdTime = createdTime
        this.updatedTime = updatedTime
    }
}