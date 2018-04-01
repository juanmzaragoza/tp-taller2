import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent }       from './login/login.component'


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
    ]
})
export class RoutingModule {}
