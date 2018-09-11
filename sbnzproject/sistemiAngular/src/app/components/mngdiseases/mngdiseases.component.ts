import { Component, OnInit, TemplateRef } from '@angular/core';
import { DiseaseService } from '../../services/disease/disease.service';
import { BsModalService, BsModalRef } from 'ngx-bootstrap';
import { Disease } from '../../model/disease';

@Component({
  selector: 'app-mngdiseases',
  templateUrl: './mngdiseases.component.html',
  styleUrls: ['./mngdiseases.component.css']
})
export class MngdiseasesComponent implements OnInit {

  diseases:Disease[]=[];

  constructor(private diseaseService:DiseaseService,
    private modalService: BsModalService) {
    this.diseaseService.getAll().subscribe(
      data=>{
        this.diseases=data;
      }
    );
   }
   modalRef: BsModalRef;
   disease:Disease;
  ngOnInit() {
  }

  openEdit(template: TemplateRef<any>, name: string){
    this.diseaseService.getDiseaseByName(name).subscribe(data=>{
      console.log(data);
      this.disease=data;
      this.modalRef = this.modalService.show(template);
    })
  }
  editDisease(){
    this.diseaseService.update(this.disease).subscribe();
    console.log(this.disease);
    this.modalService.hide(1);
  }
  delete(id:number){
    this.diseaseService.delete(id).subscribe();
    alert("successful");
  }
}
