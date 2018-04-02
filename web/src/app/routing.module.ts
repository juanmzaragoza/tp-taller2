import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent }       from './components/login/login.component'
import { HomeComponent }       from './components/home/home.component'


import { HttpClient }           from '@angular/common/http';
import { LoginService }         from './services/login/login.service';
import { UserService }          from './services/user/user.service';

const loginRoutes: Routes = [
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'home',
        component: HomeComponent
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
