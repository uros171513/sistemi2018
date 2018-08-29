package com.sbnz.sbnzproject.model;

public class SalienceDiseaseChecker {

    //private final static int allSatisfiedOffset = 2;

    public int salience = 0;
    protected int maxSymptomNum=0;
    protected boolean allSatisfied = false;

    public SalienceDiseaseChecker() {}


    public SalienceDiseaseChecker(int maxSymptomNum) {
        this.salience = 0;
        this.maxSymptomNum = maxSymptomNum;
        this.allSatisfied = false;
    }


    public void addSymptom(){
        if (this.allSatisfied)
            return;

        this.salience++;

        if (this.salience == this.maxSymptomNum){
            //this.salience += allSatisfiedOffset;
            this.allSatisfied = true;
        }
    }

    public void removeSymptom(){
        //if(allSatisfied)
          //  this.salience -= allSatisfiedOffset;
        this.salience--;
    }


	public int getSalience() {
		return salience;
	}


	public void setSalience(int salience) {
		this.salience = salience;
	}


	public int getMaxSymptomNum() {
		return maxSymptomNum;
	}


	public void setMaxSymptomNum(int maxSymptomNum) {
		this.maxSymptomNum = maxSymptomNum;
	}


	public boolean isAllSatisfied() {
		return allSatisfied;
	}    

}
