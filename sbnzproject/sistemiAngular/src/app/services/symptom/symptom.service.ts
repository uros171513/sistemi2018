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

  getSymptom(id):Observable<Symptom>{
    return this.http.get<Symptom>("http://localhost:8000/api/symptom/get/"+id);
  }

  update(symptom:Symptom):Observable<Symptom>{
    let params= JSON.stringify(symptom);
    let headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    return this.http.put<Symptom>("http://localhost:8000/api/symptom/update",params,{headers:headers});
  }

  delete(id):Observable<Boolean>{
    let headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    return this.http.delete<Boolean>("http://localhost:8000/api/symptom/delete/"+id, {headers:headers});
  }

  create(s:Symptom):Observable<Symptom>{
    let params= JSON.stringify(s);
    let headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    return this.http.post<Symptom>("http://localhost:8000/api/symptom/create",params,{headers:headers});
  }
}

