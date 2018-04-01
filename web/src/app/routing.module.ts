import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent }       from './components/login/login.component'


import { HttpClient }           from '@angular/common/http';
import { LoginService }         from './services/login/login.service'

const loginRoutes: Routes = [
    {
        path: 'login',
        component: LoginComponent
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
        HttpClient
    ]
})
export class RoutingModule {}
