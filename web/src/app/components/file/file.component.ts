import { Component, OnInit, EventEmitter, OnDestroy } from '@angular/core'
import { MaterializeDirective, MaterializeAction, toast} from "angular2-materialize";
import { File }             from '../../models/file'
import { FileService }      from '../../services/file/file.service'

declare var $ :any;
declare var M_Modal : any;

@Component({
    templateUrl: './file.component.html'
})
export class FileComponent {
    title: string;
    files: Array<File>;
    public file:File = new File();
    constructor(public FileServ: FileService){
        this.files = []
        this.title = 'File'
    }
    
    ngOnInit() {
      this.get()
    }

    private get(){
        
    }
}
