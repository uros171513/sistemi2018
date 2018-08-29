import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoggedUtils } from './utils/logged.utils';
import { UserService } from './services/user/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  constructor(private router:Router,
            private userService:UserService){}

  ngOnInit(){
  }

  isLoggedIn(){
    return !LoggedUtils.isEmpty();
  }

  logout(){
    this.userService.logout().subscribe(data=>{
      if(data){
        localStorage.removeItem("loggedUser");
        this.router.navigate(['/login']);
      }else{
        alert("Something went wrong!!!");
      }
    });
  }

  getRole(){
    return LoggedUtils.getRole();
  }

}
