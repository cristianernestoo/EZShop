package it.polito.ezshop.model;

import it.polito.ezshop.exceptions.InvalidLocationException;


public class mPosition {
    private Integer aisleNumber;
    private String rackAlphabeticIdentifier;
    private Integer levelNumber;

    public mPosition(Integer aisleNumber, String rackAlphabeticIdentifier, Integer levelNumber) throws InvalidLocationException {
        if(aisleNumber == null || aisleNumber <0 || rackAlphabeticIdentifier ==null || rackAlphabeticIdentifier.equals("") || levelNumber == null || levelNumber < 0) throw new InvalidLocationException();
        this.aisleNumber = aisleNumber;
        this.rackAlphabeticIdentifier = rackAlphabeticIdentifier;
        this.levelNumber = levelNumber;
    }

    public mPosition(String location) throws InvalidLocationException {
        if(location == null || location.equals("")) throw new InvalidLocationException();
        try {
            String [] a = location.split("[-]");
            if(a.length < 3 || a[1].equals("")) throw new InvalidLocationException();
            aisleNumber = Integer.parseInt(a[0]);
            rackAlphabeticIdentifier = a[1].toLowerCase();
            levelNumber = Integer.parseInt(a[2]);

        } catch (NumberFormatException e){
            throw new InvalidLocationException();
        }
    }

    public Integer getAisleNumber() {
        return aisleNumber;
    }

    public void setAisleNumber(Integer aisleNumber) throws InvalidLocationException {
        if (aisleNumber == null || aisleNumber < 0) throw new InvalidLocationException();
        this.aisleNumber = aisleNumber;
    }

    public String getRackAlphabeticIdentifier() {
        return rackAlphabeticIdentifier;
    }

    public void setRackAlphabeticIdentifier(String rackAlphabeticIdentifier) throws InvalidLocationException  {
        if (rackAlphabeticIdentifier == null || rackAlphabeticIdentifier.equals("")) throw new InvalidLocationException();
        this.rackAlphabeticIdentifier = rackAlphabeticIdentifier;
    }

    public Integer getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(Integer levelNumber) throws InvalidLocationException{
        if (levelNumber == null || levelNumber < 0) throw new InvalidLocationException();
        this.levelNumber = levelNumber;
    }

    public String toString() {
        return  aisleNumber + "-" + rackAlphabeticIdentifier + "-" + levelNumber;
    }

    public boolean equals(mPosition mp){
        if (mp == null) return false;
        return ((this.aisleNumber.equals(mp.aisleNumber)) && (this.levelNumber.equals(mp.levelNumber)) && (this.rackAlphabeticIdentifier.equals(mp.rackAlphabeticIdentifier)));
    }

}
