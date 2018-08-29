import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Medicine } from '../../model/medicine';

@Injectable({
  providedIn: 'root'
})
export class MedicineService {

  constructor(private http:HttpClient) { }

  getAllMedicines():Observable<Medicine[]>{
    return this.http.get<Medicine[]>("http://localhost:8000/api/medicine/getAll")
  }

  verifyMedicines(patientId:string, medicines:Medicine[]):Observable<any>{
    var params=JSON.stringify(medicines);
    let headers:HttpHeaders = new HttpHeaders({'Content-Type':'application/json;charset=utf-8'});
    return this.http.post<any>("http://localhost:8000/api/medicine/verifyMedicines/"+patientId,params,{headers:headers});
  }
}
