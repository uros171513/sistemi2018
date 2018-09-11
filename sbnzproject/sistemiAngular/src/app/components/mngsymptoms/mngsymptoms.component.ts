import { Component, OnInit, TemplateRef } from '@angular/core';
import { SymptomService } from '../../services/symptom/symptom.service';
import { Symptom } from '../../model/symptom';
import { BsModalService, BsModalRef } from 'ngx-bootstrap';

@Component({
  selector: 'app-mngsymptoms',
  templateUrl: './mngsymptoms.component.html',
  styleUrls: ['./mngsymptoms.component.css']
})
export class MngsymptomsComponent implements OnInit {

  symptoms:Symptom[]=[];

  constructor(private symptomService:SymptomService,
    private modalService: BsModalService) {
    this.symptomService.getAllSymptoms().subscribe(
      data=>{
        this.symptoms=data;
      }
    );
   }
   modalRef: BsModalRef;
   symptom:Symptom;
  ngOnInit() {
  }

  openEdit(template: TemplateRef<any>, id: number){
    this.symptomService.getSymptom(id).subscribe(data=>{
      console.log(data);
      this.symptom=data;
      this.modalRef = this.modalService.show(template);
    })
  }
  editSymptom(){
    this.symptomService.update(this.symptom).subscribe();
    console.log(this.symptom);
    this.modalService.hide(1);
  }
  delete(id:number){
    this.symptomService.delete(id).subscribe();
    alert("successful");
  }

}
