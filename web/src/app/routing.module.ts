import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent }       from './components/login/login.component'
import { HomeComponent }       from './components/home/home.component'
import { ServerComponent }       from './components/server/server.component'
import { StatusComponent }       from './components/status/status.component'
import { FileComponent }        from './components/file/file.component'
import { UserListComponent }    from './components/user/user.list.component'


import { HttpClient }           from '@angular/common/http';
import { LoginService }         from './services/login/login.service';
import { UserService }          from './services/user/user.service';
import {AuthGuard}              from './services/remote/auth.guard';

const loginRoutes: Routes = [
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'home',
        canActivate: [AuthGuard],
        component: HomeComponent
    },
    {
        path: 'server',
        canActivate: [AuthGuard],
        component: ServerComponent
    },
    {
        path: 'status',
        canActivate: [AuthGuard],
        component: StatusComponent
    },
    {
        path: 'file',
        canActivate: [AuthGuard],
        component: FileComponent
    },
    {
        path: 'user',
        canActivate: [AuthGuard],
        component: UserListComponent
    }
];

@NgModule({
    imports:[
        RouterModule.forRoot(loginRoutes, { useHash: true })
    ],
    exports: [
        RouterModule
    ],
    providers: [
        LoginService,
        HttpClient,
        UserService
    ]
})
export class RoutingModule {}
