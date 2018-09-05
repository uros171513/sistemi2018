import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoggedUtils } from './utils/logged.utils';
import { UserService } from './services/user/user.service';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import $ from 'jquery';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  mMessage: string;
  private serverUrl = 'http://localhost:8000/socket';
  private stompClient;

  constructor(private router:Router,
            private userService:UserService){
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    this.stompClient.connect({}, frame =>{
      this.stompClient.subscribe('/chat', message =>{
        this.mMessage = message.body;
        $(".notif").append("<div class='message'>"+message.body+"</div>")
        console.log(message.body);
      });
    });
  }

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
