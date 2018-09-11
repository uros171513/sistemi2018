import { Component, OnInit } from '@angular/core';
import { Disease } from '../../model/disease';
import { DiseaseService } from '../../services/disease/disease.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-diseasenew',
  templateUrl: './diseasenew.component.html',
  styleUrls: ['./diseasenew.component.css']
})
export class DiseasenewComponent implements OnInit {

  disease:Disease={
    name:"",
    symptoms:[],
    diseaseType:-1,
    deleted:false
  }

  constructor(private diseaseService:DiseaseService,
  private router:Router) { }

  ngOnInit() {
  }

  addNewDisease(){
    this.diseaseService.create(this.disease).subscribe();
    this.router.navigate(['/home']);
  }
}
