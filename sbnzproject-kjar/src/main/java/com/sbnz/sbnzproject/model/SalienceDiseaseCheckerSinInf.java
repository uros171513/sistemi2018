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

    public int getSalienceSinusInfectionSpecial(){
        if(this.specialEvaluated)
            return this.salience;
        this.specialEvaluated = true;
        this.addSymptom();
        return this.salience;
    }
}