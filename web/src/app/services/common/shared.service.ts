import { Component, Injectable, EventEmitter } from '@angular/core';


@Injectable()
export class SharedService {
    startAccount = new EventEmitter<any>();
} 

