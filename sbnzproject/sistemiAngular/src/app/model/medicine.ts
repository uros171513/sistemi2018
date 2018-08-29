import { MedicineComponent } from "./medicineComponent";
import { User } from "./user";

export interface Medicine{
    name:string, 
    components:MedicineComponent[],
	medicineType:number,
    deleted:boolean,
    doctor:User
}