package com.sbnz.sbnzproject.model;

public class SalienceDiseaseCheckerSinInf extends SalienceDiseaseChecker {

    private boolean specialEvaluated = false;

    public SalienceDiseaseCheckerSinInf(){
        super();
        this.specialEvaluated = false;
    }

    public SalienceDiseaseCheckerSinInf(int maxSymptomNum) {
        super(maxSymptomNum);
        this.specialEvaluated = false;
    }

//    public int getSalienceSinusInfectionSpecial(){
//        if(this.specialEvaluated) {
//        	System.err.println(this.salience);
//            return this.salience;
//        }
//        this.specialEvaluated = true;
//        this.addSymptom();
//        return this.salience;
//    }
    
    public void addSymptom(){
        if (this.allSatisfied)
            return;

        this.salience++;

        if (this.salience == this.maxSymptomNum){
            //this.salience += allSatisfiedOffset;
            this.allSatisfied = true;
        }
    }
}