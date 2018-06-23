import { Component, OnInit, EventEmitter, OnDestroy } from '@angular/core'
import { MaterializeDirective, MaterializeAction, toast} from "angular2-materialize";
import { File }             from '../../models/file'
import { FileService }      from '../../services/file/file.service'

declare var $ :any;

@Component({
    templateUrl: './file.component.html'
})
export class FileComponent {
    title: string;
    files: Array<File>;
    uploadByFirebase: boolean;
    public file:File = new File();
    constructor(public FileServ: FileService){
        this.files = []
        this.title = 'File'
        this.uploadByFirebase = false
    }
    
    ngOnInit() {
      this.get()
    }
    open(){
        this.openModal();
        $('.modal').modal();
          setTimeout(()=>{
            $('input[name=filename]').focus()
          }, 1000);
    }
    modalActions = new EventEmitter<string|MaterializeAction>();
    openModal() {
      this.modalActions.emit({action:"modal",params:['open']});
    }
    closeModal() {
      var me = this
      me.file = new File()
      this.modalActions.emit({action:"modal",params:['close']});
    }
    save(file :File){
        var vm = this
        console.info(this)
        if(file.id){
          file.updatedTime = Date.now()
          console.log("edit", file)
          //me.update(file);
        }
        else{
          file.createdTime = Date.now()
          if(vm.uploadByFirebase){
            vm.createbyFirebase(file);
          }
          else{

          }
        }
    }

    onFileChange(event:any) {
        let reader = new FileReader();
        var vm = this
        if(event.target.files && event.target.files.length > 0) {
          let file = event.target.files[0];
          reader.readAsDataURL(file);
          reader.onload = () => {
            vm.file.size = file.size
            vm.file.resource = reader.result.split(',')[1]
          };
        }
      }
      /*
      update(serv: Server){
        var me = this
        me.ServerServ.update(serv).subscribe((res) => {
          me.servers = me.JsonServ.removeItem(me.servers, {id:serv.id})
          me.servers.push(serv)
          me.server = new Server()
          toast("the server was updated",4000)
        },
        error =>{
          console.log(error)
        });
      }*/
      createbyFirebase(file: File){
        var vm = this
        vm.FileServ.createbyFirebase(file).subscribe((file) => {
          vm.files.push(file)
          vm.file = new File()
          toast("the file was created",4000)
        },
        error =>{
          console.log(error)
        });
      }

    private get(){
        var vm:any = this
        vm.FileServ.get().subscribe((files: Array<File>) =>{
            vm.files = files
        })
    }
}
