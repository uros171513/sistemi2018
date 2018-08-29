import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MedicalService } from '../../services/medical/medical.service';
import { MedicalRecord } from '../../model/medicalRecord';
import { Symptom } from '../../model/symptom';
import { Disease } from '../../model/disease';
import { User } from '../../model/user';

@Component({
  selector: 'app-medical-record',
  templateUrl: './medical-record.component.html',
  styleUrls: ['./medical-record.component.css']
})
export class MedicalRecordComponent implements OnInit {

  constructor(private route:ActivatedRoute,
  private medicalService:MedicalService) { }

  id=-1;

  symptom:Symptom={
    name:"",
    symptomType:"",
    deleted:false
  }

  disease:Disease={
    name:"",
    symptoms:null,
    diseaseType:-1,
    deleted:false
  }

  doctor:User={
    username:"",
    password:"",
    name:"",
    lastName:"",
    role:-1
  }

  medicalRecord:MedicalRecord={
    disease:this.disease,
    symptoms:null,
    date:null,
    medicine:null,
    doctor:null
  };

  ngOnInit() {
    this.route.params.subscribe( params => this.id=params['id']);
    this.medicalService.getMedicalRecord(this.id).subscribe(
      data => {
        console.log(data);
        this.medicalRecord= data;
      }
    );
  }

}
