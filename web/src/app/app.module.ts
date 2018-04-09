import { MaterializeModule } from 'angular2-materialize';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RoutingModule } from './routing.module'

import { SharedService } from './services/common/shared.service'

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component'
import { HomeComponent } from './components/home/home.component'
import { UserComponent } from './components/user/user.component'


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    UserComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MaterializeModule,
    RoutingModule,
    FormsModule    
  ],
  providers: [SharedService],
  bootstrap: [AppComponent]
})
export class AppModule { }
