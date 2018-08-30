import { Component, OnInit, TemplateRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PatientService } from '../../services/patient/patient.service';
import { Patient } from '../../model/patient';
import { MedicalRecord } from '../../model/medicalRecord';
import { Disease } from '../../model/disease';
import { SelectedSymptoms } from '../../model/selectedSymptoms';
import { MedicalService } from '../../services/medical/medical.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { SymptomService } from '../../services/symptom/symptom.service';
import { Symptom } from '../../model/symptom';
import { DiseaseService } from '../../services/disease/disease.service';
import { MedicineService } from '../../services/medicine/medicine.service';
import { Medicine } from '../../model/medicine';
import { SelectedMedicine } from '../../model/selectedMedicine';

@Component({
  selector: 'app-patient-view',
  templateUrl: './patient-view.component.html',
  styleUrls: ['./patient-view.component.css']
})
export class PatientViewComponent implements OnInit {

  patient:Patient={
    name:"",
    lastName:"",
    patientHistory:[],
    medicineAllergies:[],
    componentAllergies:[]
  };
  patientId;

  possibleDisease:Disease={
    name:"",
    symptoms:null,
    diseaseType:-1,
    deleted:false
  }

  selectedSymptoms:SelectedSymptoms[]=[];
  realSelectedSymptoms:SelectedSymptoms[]=[];

  newMedicalRecord:MedicalRecord={
    disease:null,
    symptoms:null,
    medicine:null,
    date:null,
    doctor:null
  };

  modalRef: BsModalRef;

  symptom:Symptom={
    name:"",
    symptomType:null,
    deleted:false
  }

  symptoms:Symptom[];

  choosenDisease={
    name:""
  }

  choosenDisease1={
    name:""
  }

  medicalRecord:MedicalRecord=null;
  noDisease:string="";

  medicines:Medicine[];

  selectedMedicines:SelectedMedicine[]=[];
  realSelectedMedicines:SelectedMedicine[]=[];

  verifyMessage:string="";

  relatedDiseases:Disease[]=[];

  allDiseases:Disease[]=[];

  personalOpinionBoolean:boolean=false;
  relatedSymptoms:Symptom[]=[];

  historyId:number=-1;

  constructor(private patientService:PatientService,
    private medicalService:MedicalService,
    private medicineService:MedicineService,
    private diseaseService:DiseaseService,
    private symptomService:SymptomService,
    private route: ActivatedRoute,
    private router:Router,
    private modalService: BsModalService) { }

  getId(){
    this.route.params.subscribe( params => this.patientId=params['id']);
    this.patientService.getPatient(this.patientId).subscribe(
      data => {
        this.patient = data;
      }
    );
  }

  go(ph:string){
    this.router.navigate(['medicalRecord/',ph]);
  }

  ngOnInit() {
    this.personalOpinionBoolean=false;
    this.medicines=[];
    this.medicalRecord=null;
    this.selectedSymptoms=[];
    this.selectedMedicines=[];
    this.relatedDiseases=[];
    this.relatedSymptoms=[];
    this.getId();
  }

  cancel(){
    this.personalOpinionBoolean=false;
    this.verifyMessage="";
    this.noDisease="";
    this.medicines=[];
    this.medicalRecord=null;
    this.selectedSymptoms=[];
    this.selectedMedicines=[];
    this.relatedDiseases=[];
    this.relatedSymptoms=[];
    this.modalService.hide(1);
  }

  openModal(template: TemplateRef<any>) {
    this.relatedSymptoms=[];
    this.personalOpinionBoolean=false;
    this.verifyMessage="";
    this.noDisease="";
    this.medicines=[];
    this.medicalRecord=null;
    this.selectedSymptoms=[];
    this.selectedMedicines=[];
    this.relatedDiseases=[];
    this.allDiseases=[];
    this.realSelectedMedicines=[];
    this.realSelectedSymptoms=[];
    this.modalRef = this.modalService.show(template);
    this.symptomService.getAllSymptoms().subscribe(
      data=>{
        this.symptoms=data;
        for(let s of this.symptoms){
          if(!s.deleted){
            let selected={
              symptom:s,
              selected:false
            }
            console.log(selected);
            this.selectedSymptoms.push(selected);
          }
        }
      }
    );
    this.diseaseService.getAll().subscribe(
      data=>{
        this.allDiseases=data;
      }
    );
  }  

  getSelectedSymptoms(){
    this.realSelectedSymptoms = this.selectedSymptoms.filter(s => {
      return s.selected;
    });
  }

  symptomsByDisease(){
    for(let d of this.allDiseases){
      if(this.choosenDisease.name==d.name){
        this.symptomService.getRelatedSymptoms(d).subscribe(data=>{
          this.relatedSymptoms=data;
        });
      }
    }
  }

  diseaseBySymptoms(){
    this.medicalRecord=null;
    this.medicines=[];
    var patientsSymptoms:Symptom[]=[];
    for(let s of this.realSelectedSymptoms){
      patientsSymptoms.push(s.symptom);
    }
    
    this.selectedMedicines=[];
    this.diseaseService.mostLikelyDisease(this.patientId, patientsSymptoms).subscribe(data=>{
      if(data['disease']!=null){
        this.medicalRecord=data;
        this.medicines=[];
        this.medicineService.getAllMedicines().subscribe(data=>{
          this.medicines=data;
          for(let m of this.medicines){
            if(!m.deleted){
              let selected={
                medicine:m,
                selected:false
              }
              this.selectedMedicines.push(selected);
            }
          }
        })
      }else{
        this.noDisease="There's no disease for the passed symptoms!";
      }

    });
    this.noDisease="";
  }

  getSelectedMedicines(){
    this.realSelectedMedicines = this.selectedMedicines.filter(s => {
      return s.selected;
    });
    console.log(this.realSelectedMedicines.length)
  }

  setMedicines(){
    var patientsMedicines:Medicine[]=[];
    for(let m of this.realSelectedMedicines){
      patientsMedicines.push(m.medicine);
    }
    this.medicineService.verifyMedicines(this.patientId, patientsMedicines).subscribe(data=>{
      console.log("Verifikovanje:");
      console.log(data);
      if(data){
        this.verifyMessage="Patient is not allergic to any of the selected medicines!";
        this.medicalRecord['medicine']=patientsMedicines;
      }else{
        this.verifyMessage="Patient is allergic to some of the selected medicines, you must choose some other medicines!";
      }
    })
    this.verifyMessage="";
  }

  addNewSession(){
    if(this.medicalRecord['medicine'].length==0){
      console.log("Ne moze da se pritisne!");
    }else{
      this.medicalService.createMedicalRecord(this.patientId,this.medicalRecord).subscribe(data=>{
        console.log(data);
      })
      this.cancel();
    }
    
    this.personalOpinionBoolean=false;
  }

  getRelatedDiseases(){
    //poslati simptome
    var patientsSymptoms:Symptom[]=[];
    for(let s of this.realSelectedSymptoms){
      patientsSymptoms.push(s.symptom);
    }
    this.diseaseService.getRelatedDiseases(patientsSymptoms).subscribe(data=>{
      console.log(data);
      this.relatedDiseases=data;
    });

  }

  personalOpinion(){
    this.medicalRecord=null;
    this.personalOpinionBoolean=true;
  }

  next(){
    var patientsSymptoms:Symptom[]=[];
    for(let s of this.realSelectedSymptoms){
      patientsSymptoms.push(s.symptom);
    }
    this.selectedMedicines=[];
    this.diseaseService.getDiseaseByName(this.choosenDisease1.name).subscribe(
      data=>{
        this.medicalRecord={
          disease:data,
          symptoms:patientsSymptoms,
          medicine:null,
          date:null,
          doctor:null
        };
        this.medicines=[];
        this.medicineService.getAllMedicines().subscribe(data=>{
          console.log(data);
          this.medicines=data;
          for(let m of this.medicines){
            if(!m.deleted){
              let selected={
                medicine:m,
                selected:false
              }
              this.selectedMedicines.push(selected);
            }
          }
        })
      }
    );

    

  }

}
