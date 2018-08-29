import { MedicalRecord } from "./medicalRecord";
import { Medicine } from "./medicine";
import { MedicineComponent } from "./medicineComponent";

export interface Patient{
    name:string,
    lastName:string,
    patientHistory:MedicalRecord[],
    medicineAllergies:Medicine[],
    componentAllergies:MedicineComponent[]
}