import { Symptom } from "./symptom";

export interface Disease{
    name:string,
    symptoms:Symptom[],
    diseaseType:number,
	deleted:boolean
}