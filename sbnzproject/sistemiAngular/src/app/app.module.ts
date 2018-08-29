import { BrowserModule } from '@angular/platform-browser';
import { NgModule, forwardRef } from '@angular/core';

import { AppComponent } from './app.component';


import { FormsModule, NG_VALUE_ACCESSOR } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { HomeComponent } from './components/home/home.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { UserService } from './services/user/user.service';
import { ErrorInterceptorService } from './services/error-interceptor/error-interceptor.service';
import { TokenInterceptorService } from './services/token-interceptor/token-interceptor.service';
import { PatientsComponent } from './components/patients/patients.component';
import { ModalModule } from 'ngx-bootstrap';
import { PatientViewComponent } from './components/patient-view/patient-view.component';
import { DiseaseService } from './services/disease/disease.service';
import { PatientService } from './services/patient/patient.service';
import { MedicalService } from './services/medical/medical.service';
import { ComponentService } from './services/component/component.service';
import { MedicineService } from './services/medicine/medicine.service';
import { MedicalRecordComponent } from './components/medical-record/medical-record.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    HomeComponent,
    PageNotFoundComponent,
    PatientsComponent,
    PatientViewComponent,
    MedicalRecordComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ModalModule.forRoot(),
    RouterModule.forRoot(
      [
        { path: '', component: HomeComponent, pathMatch: 'full'},
        { path: 'login', component: LoginComponent },
        { path: 'register', component: RegistrationComponent },
        { path: 'home', component: HomeComponent },
        { path: 'patients', component: PatientsComponent },
        { path: 'patient/:id', component: PatientViewComponent },
        { path: 'medicalRecord/:id', component: MedicalRecordComponent },
        { path: '**', redirectTo:'not-found' }
      ]
    )
  ],
  providers: [
    UserService,
    DiseaseService,
    PatientService,
    MedicalService,
    TokenInterceptorService,
    ComponentService,
    MedicineService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptorService,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptorService,
      multi: true
    }, 
    { 
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: forwardRef(() => PatientsComponent),
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
