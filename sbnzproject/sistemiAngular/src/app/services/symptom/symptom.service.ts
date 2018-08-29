import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Symptom } from '../../model/symptom';
import { Disease } from '../../model/disease';

@Injectable({
  providedIn: 'root'
})
export class SymptomService {

  constructor(private http:HttpClient) { }

  getAllSymptoms():Observable<Symptom[]>{
    return this.http.get<Symptom[]>("http://localhost:8000/api/symptom/getAll")
  }

  
  getRelatedSymptoms(disease:Disease):Observable<Symptom[]>{
    let headers:HttpHeaders = new HttpHeaders({'Content-Type':'application/json;charset=utf-8'});
    let param = JSON.stringify(disease);
    return this.http.post<Symptom[]>("http://localhost:8000/api/symptom/getRelatedSymptoms",param,{headers:headers});
  }
}
