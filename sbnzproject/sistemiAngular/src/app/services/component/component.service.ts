import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MedicineComponent } from '../../model/medicineComponent';

@Injectable({
  providedIn: 'root'
})
export class ComponentService {

  constructor(private http:HttpClient) { }

  getAllComponents():Observable<MedicineComponent[]>{
    return this.http.get<MedicineComponent[]>("http://localhost:8000/api/component/getAll")
  }
}
