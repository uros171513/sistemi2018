import { Component, OnInit } from '@angular/core';
import { Symptom } from '../../model/symptom';
import { SymptomService } from '../../services/symptom/symptom.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-symptomnew',
  templateUrl: './symptomnew.component.html',
  styleUrls: ['./symptomnew.component.css']
})
export class SymptomnewComponent implements OnInit {

  symptom:Symptom={
    name:"",
    symptomType:"",
    deleted:false
  }

  constructor(private symptomService:SymptomService, private router:Router) { }

  ngOnInit() {
  }
  addNewSymptom(){
    this.symptomService.create(this.symptom).subscribe();
    this.router.navigate(['/home']);
  }
}
