import { Injectable } from '@angular/core';
import { MedicalRecord } from '../../model/medicalRecord';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MedicalService {

  constructor(private http:HttpClient) { }
  
  createMedicalRecord(patientId:number, medicalRecord:MedicalRecord){
    let headers:HttpHeaders = new HttpHeaders({'Content-Type':'application/json;charset=utf-8'});
    let param = JSON.stringify(medicalRecord);
    return this.http.post("http://localhost:8000/api/medicalRecord/create/"+patientId,param,{headers:headers} );
  }

  getMedicalRecord(id:number):Observable<MedicalRecord>{
    let param = JSON.stringify(id);
    return this.http.get<MedicalRecord>("http://localhost:8000/api/medicalRecord/get/"+id);
  }
}
