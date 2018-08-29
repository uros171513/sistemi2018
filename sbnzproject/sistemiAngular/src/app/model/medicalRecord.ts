import { Disease } from "./disease";
import { Symptom } from "./symptom";
import { Medicine } from "./medicine";
import { User } from "./user";

export interface MedicalRecord{
    disease:Disease,
    symptoms:Symptom[],
    medicine:Medicine[],
    date:Date,
    doctor:User
}