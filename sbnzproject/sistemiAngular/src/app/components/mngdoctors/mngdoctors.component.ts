import { Component, OnInit, TemplateRef } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { User } from '../../model/user';
import { Router } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';

@Component({
  selector: 'app-mngdoctors',
  templateUrl: './mngdoctors.component.html',
  styleUrls: ['./mngdoctors.component.css']
})
export class MngdoctorsComponent implements OnInit {

  doctors:User[]=[];
  repeatedPasswordText:string="";
  constructor(private userService:UserService,
  private router:Router,
  private modalService: BsModalService) {
    this.userService.getAll().subscribe(
      data=>{
        this.doctors=data;
      }
    );
   }

  ngOnInit() {
  }
  modalRef: BsModalRef;
  user:User;
  openEdit(template: TemplateRef<any>, id: number){
    this.userService.getDoctor(id).subscribe(data=>{
      console.log(data);
      this.user=data;
      this.user['password']="";
      this.modalRef = this.modalService.show(template);
    })
  }
  editDoctor(){
    if(this.user.password!=this.repeatedPasswordText){
      console.log("passwords don't match");
      alert("passwords don't match");
      this.repeatedPasswordText="";
      return;
    }

    this.userService.update(this.user).subscribe();
    console.log(this.user);
    this.userService.getAll().subscribe(
      data=>{
        this.doctors=data;
      }
    );
    this.modalService.hide(1);
  }
  delete(id:number){
    this.userService.delete(id).subscribe();
    this.userService.getAll().subscribe(
      data=>{
        this.doctors=data;
      }
    );
    alert("successful");
  }


}
