import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient:HttpClient) { }

  authenticate(username: string, password: string):Observable<any>{
    let authenticationRequest = { username: username, password: password };
    let params = JSON.stringify(authenticationRequest);
    let headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    return this.httpClient.post<any>("http://localhost:8000/auth", params,
      {
        headers: headers
      });
  }

  
  login(username: string, password: string):Observable<any>{
    let authenticationRequest = { username: username, password: password };
    let params = JSON.stringify(authenticationRequest);
    let headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    return this.httpClient.post<any>("http://localhost:8000/api/user/login",params,{
      headers: headers
    });
  }
  
  logout():Observable<Boolean>{
    return this.httpClient.get<Boolean>("http://localhost:8000/api/user/logout");
  }

  registration(user:User):Observable<string>{
    user.role=1;
    let params= JSON.stringify(user);
    let headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    return this.httpClient.post<string>("http://localhost:8000/api/user/registerDoctor",params,{headers:headers});
  }

}
