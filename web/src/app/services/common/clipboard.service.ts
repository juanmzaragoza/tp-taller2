import { Injectable } from '@angular/core';

@Injectable()
export class ClipBoardService {
    constructor(){}
    copy = (text:string): void=>{
        // Crea un campo de texto "oculto"
        var aux = document.createElement("input");
        // Asigna el contenido del elemento especificado al valor del campo
        aux.setAttribute("value", text);
        // Añade el campo a la página
        document.body.appendChild(aux);
        // Selecciona el contenido del campo
        aux.select();
        // Copia el texto seleccionado
        document.execCommand("copy");
        // Elimina el campo de la página
        document.body.removeChild(aux);
        return
    }
} 
