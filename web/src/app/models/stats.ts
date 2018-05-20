export class Stats{
    public _id: string
    public _rev: string
    public numUsers: Number
    public numUsersActiveToday: Number
    public numStoriesToday: Number
    public numFastStoriesToday: Number
    public numStories: Number
    public numUsersMessages: Number
    public numUsersMessagesToday: Number
    public numAcceptedContactsToday: Number
    constructor(){
        this._id = '', this._rev = ''
        this.numUsers = 0, this.numUsersActiveToday = 0,
        this.numStoriesToday = 0,this.numFastStoriesToday = 0,
        this.numStories = 0, this.numUsersMessages = 0,
        this.numUsersMessagesToday = 0,
        this.numAcceptedContactsToday = 0
    }
}