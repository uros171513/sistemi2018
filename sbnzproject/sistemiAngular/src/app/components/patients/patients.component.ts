import { Component, OnInit, TemplateRef } from '@angular/core';
import { Patient } from '../../model/patient';
import { PatientService } from '../../services/patient/patient.service';
import { Router } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { Medicine } from '../../model/medicine';
import { MedicineService } from '../../services/medicine/medicine.service';
import { SelectedMedicine } from '../../model/selectedMedicine';
import { SelectedComponent } from '../../model/selectedComponent';
import { ComponentService } from '../../services/component/component.service';
import { MedicineComponent } from '../../model/medicineComponent';

@Component({
  selector: 'app-patients',
  templateUrl: './patients.component.html',
  styleUrls: ['./patients.component.css']
})
export class PatientsComponent implements OnInit {

  patients:Patient[];

  patient:Patient={
    name:"",
    lastName:"",
    patientHistory:[],
    medicineAllergies:[],
    componentAllergies:[]
  }

  chronic:Patient[]=[];
  addicts:Patient[]=[];
  modalRef: BsModalRef;

  medicines:Medicine[];

  components:MedicineComponent[];

  component:MedicineComponent={
    name:"",
    deleted:false
  }

  medicine:Medicine={
    name:"",
    components:[],
	  medicineType:-1,
    deleted:false,
    doctor:null
  }
  
  selectedMedicines:SelectedMedicine[]=[];
  realSelectedMedicines:SelectedMedicine[]=[];

  selectedComponents:SelectedComponent[]=[];
  realSelectedComponents:SelectedComponent[]=[];

  allowChronic:boolean=false;
  allowAddicts:boolean=false;

  constructor(private patientService:PatientService,
  private medicineService:MedicineService,
  private componentService:ComponentService,
  private router:Router,
  private modalService: BsModalService) { }

  ngOnInit() {
    
    this.allowChronic=false;
    this.allowAddicts=false;
    this.patientService.getAllPatients().subscribe(data=>{
      this.patients=data;
    });

    this.selectedMedicines=[];
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
    });

    this.selectedComponents=[];
    this.componentService.getAllComponents().subscribe(
      data=>{
        this.components=data;
        for(let c of this.components){
          if(!c.deleted){
            let selected={
              component:c,
              selected:false
            }
            this.selectedComponents.push(selected);
          }
        }
      }
    );
  }

  refresh(){
    this.allowChronic=false;
    this.allowAddicts=false;
    this.realSelectedComponents=[];
    this.selectedComponents=[];
    this.realSelectedMedicines=[];
    this.selectedMedicines=[];

    this.componentService.getAllComponents().subscribe(
      data=>{
        this.components=data;
        for(let c of this.components){
          if(!c.deleted){
            let selected={
              component:c,
              selected:false
            }
            this.selectedComponents.push(selected);
          }
        }
      }
    );

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
    });
  }

  go(patientId:number){
    
    this.allowChronic=false;
    this.allowAddicts=false;
    this.patientService.getPatient(patientId).subscribe(data=>{
      this.router.navigate(['patient/',data['id']]);
    })
  }

  createPatient(){
    for(let m of this.realSelectedMedicines){
      this.patient.medicineAllergies.push(m.medicine);
    }
    for(let c of this.realSelectedComponents){
      this.patient.componentAllergies.push(c.component);
    }
    console.log(this.patient);
    this.patientService.createPatient(this.patient).subscribe(data=>{
    });
    this.patient={
      name:"",
      lastName:"",
      patientHistory:[],
      medicineAllergies:[],
      componentAllergies:[]
    }
    this.refresh();
    this.modalService.hide(1);
  }

  cancel(){
    this.refresh();
    this.modalService.hide(1);
  }
  
  openModal(template: TemplateRef<any>, index: number) {
    this.modalRef = this.modalService.show(template);
  }

  getSelectedMedicines(){
    this.realSelectedMedicines = this.selectedMedicines.filter(s => {
      return s.selected;
    });
  }
    
  getSelectedComponents(){
    this.realSelectedComponents = this.selectedComponents.filter(s => {
      return s.selected;
    });
  }

  withChronic(){
    this.chronic=[];
    this.allowChronic=true;
    this.patientService.getWithChronic().subscribe(data=>{
      this.chronic=data;
    });
  }

  getAddicts(){
    this.addicts=[];
    this.allowAddicts=true;
    this.patientService.getAddicts().subscribe(data=>{
      this.addicts=data;
    });
  }

}
