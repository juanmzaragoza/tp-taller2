import { Component, Injectable } from '@angular/core';
import * as _ from 'underscore';

export interface IId{
    id:string
}


@Injectable()
export class JsonService {
    constructor(){}
    removeItem = (list: Array<IId>, data: IId): Array<IId>=>{
        var newList: Array<IId> = []
        list.forEach(item => {
            if(item.id != data.id){
                newList.push(item)
            }
        });
        return newList
    }
} 
