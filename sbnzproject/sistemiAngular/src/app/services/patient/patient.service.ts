import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Patient } from '../../model/patient';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PatientService {

  constructor(private http:HttpClient) { }

  getAllPatients():Observable<Patient[]>{
    return this.http.get<Patient[]>("http://localhost:8000/api/patient/getAll")
  }
  
  createPatient(patient:Patient){
    let headers:HttpHeaders = new HttpHeaders({'Content-Type':'application/json;charset=utf-8'});
    let param = JSON.stringify(patient);
    return this.http.post("http://localhost:8000/api/patient/create",param,{headers:headers} );
  }
  
  getPatient(patientId:number):Observable<Patient>{
    return this.http.get<Patient>("http://localhost:8000/api/patient/get/"+patientId);
  }

  getWithChronic():Observable<Patient[]>{
    return this.http.get<Patient[]>("http://localhost:8000/api/patient/chronic");
  }

  getAddicts():Observable<Patient[]>{
    return this.http.get<Patient[]>("http://localhost:8000/api/patient/addicts");
  }
}
