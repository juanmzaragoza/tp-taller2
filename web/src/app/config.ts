export const Config = {
    "sharedServer":{
        //"host": "http://127.0.0.1:8081",
        "host": "https://heroku-sharedserver.herokuapp.com",
        "prefix": "/api",
        "endPoint":{
            "token": "/token"
        }
    },
    "web":{
        "tags":[
            {
                name: "Status",
                routerLink: "status",
                title: "View Status"
            },
            {
                name: "Servers",
                routerLink: "server",
                title: "View Servers"
            },
            {
                name: "Files",
                routerLink: "file",
                title: "View Files"
            }
        ]
    }
}
