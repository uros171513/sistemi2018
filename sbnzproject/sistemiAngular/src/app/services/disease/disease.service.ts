import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Symptom } from '../../model/symptom';
import { Observable } from 'rxjs';
import { Disease } from '../../model/disease';
import { MedicalRecord } from '../../model/medicalRecord';

@Injectable({
  providedIn: 'root'
})
export class DiseaseService {

  constructor(private http:HttpClient) { }

  mostLikelyDisease(patientId:string, symptoms:Symptom[]):Observable<MedicalRecord>{
    let headers:HttpHeaders = new HttpHeaders({'Content-Type':'application/json;charset=utf-8'});
    let param = JSON.stringify(symptoms);
    return this.http.post<MedicalRecord>("http://localhost:8000/api/disease/diseaseBySymptoms/"+patientId,param,{headers:headers} );
  }

  getRelatedDiseases(symptoms:Symptom[]):Observable<Disease[]>{
    let headers:HttpHeaders = new HttpHeaders({'Content-Type':'application/json;charset=utf-8'});
    let param = JSON.stringify(symptoms);
    return this.http.post<Disease[]>("http://localhost:8000/api/disease/getRelatedDiseases",param,{headers:headers});
  }

  getAll():Observable<Disease[]>{
    return this.http.get<Disease[]>("http://localhost:8000/api/disease/getAll");
  }

  getDiseaseByName(name:string):Observable<Disease>{
    let headers:HttpHeaders = new HttpHeaders({'Content-Type':'application/json;charset=utf-8'});
    let param = JSON.stringify(name);
    return this.http.post<Disease>("http://localhost:8000/api/disease/getByName",param,{headers:headers});
  }

  create(disease:Disease):Observable<Disease>{
    let headers:HttpHeaders = new HttpHeaders({'Content-Type':'application/json;charset=utf-8'});
    let param = JSON.stringify(disease);
    return this.http.post<Disease>("http://localhost:8000/api/disease/create",param,{headers:headers});
  }

  update(disease:Disease):Observable<Disease>{
    let headers:HttpHeaders = new HttpHeaders({'Content-Type':'application/json;charset=utf-8'});
    let param = JSON.stringify(disease);
    return this.http.put<Disease>("http://localhost:8000/api/disease/update",param,{headers:headers});
  }

  delete(id:any):Observable<Boolean>{
    return this.http.delete<Boolean>("http://localhost:8000/api/disease/delete/"+id);
  }
}
