import { MaterializeModule } from 'angular2-materialize';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule }    from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RoutingModule } from './routing.module'

import { SharedService } from './services/common/shared.service'
import { JsonService } from './services/common/json.service'
import { RemoteService } from './services/remote/remote.service'
import { CookieService } from 'ngx-cookie-service';
import { TokenInterceptor } from './services/remote/token.interceptor'
import { ClipBoardService } from './services/common/clipboard.service'
import { StatusService } from './services/status/status.service'
import { ServerService } from './services/server/server.service'

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component'
import { HomeComponent } from './components/home/home.component'
import { UserComponent } from './components/user/user.component'
import { ServerComponent } from './components/server/server.component'
import { NavBarComponent } from './components/navbar/navbar.component'
import { StatusComponent } from './components/status/status.component'

import { EventKeyDirective } from './directives/event.key.directive'
import { FixButtonDirective } from './directives/fix.button.directive'


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    UserComponent,
    ServerComponent,
    NavBarComponent,
    StatusComponent,
    EventKeyDirective,
    FixButtonDirective
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MaterializeModule,
    RoutingModule,
    FormsModule,
    CommonModule
  ],
  providers: [
    SharedService,
    RemoteService,
    CookieService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    JsonService,
    ClipBoardService,
    DatePipe,
    StatusService,
    ServerService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
