import { Component, OnInit } from '@angular/core';
import { User } from '../../model/user';
import { UserService } from '../../services/user/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  user:User={
    username:"",
    password:"",
    name:"",
    lastName:"",
    role:-1
  }
  repeatedPasswordText:string="";

  constructor(private userService:UserService, private router:Router) { }

  ngOnInit() {
  }

  registerDoctor(){

    if(this.user.password!=this.repeatedPasswordText){
      console.log("passwords don't match");
      alert("passwords don't match");
      this.repeatedPasswordText="";
      return;
    }
    this.userService.registration(this.user).subscribe(data=>{console.log(data)});
    this.router.navigate(['/home']);
  }
}
