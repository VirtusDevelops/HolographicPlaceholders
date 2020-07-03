package com.neutralplasma.holographicPlaceholders.addons;

import com.neutralplasma.holographicPlaceholders.storage.SignLocation;

import java.util.HashMap;

public abstract class Addon {

    public String name = "none";

    private HashMap<SignLocation, Integer> signs = new HashMap<>();
    private HashMap<SignLocation, Integer> heads = new HashMap<>();

    private boolean isEnabled = false;

    public boolean isEnabled(){
        return this.isEnabled;
    }

    public void onEnable(){
        this.isEnabled = true;
    }

    public void onDisable(){
        this.isEnabled = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean addSign(SignLocation signLocation, int position){
        if(signs.containsKey(signLocation)){
            return false;
        }
        signs.put(signLocation, position);
        return true;
    }

    public void addSigns(HashMap<SignLocation, Integer> signs){
        this.signs = signs;
    }

    public boolean removeSign(SignLocation signLocation){
        for(SignLocation location : signs.keySet()){
            if(location.toString().equalsIgnoreCase(signLocation.toString())){
                signs.remove(location);
                return true;
            }
        }
        return false;
    }

    public HashMap<SignLocation, Integer> getSigns(){
        return this.signs;
    }

    public boolean addHead(SignLocation signLocation, int position){
        if(heads.containsKey(signLocation)){
            return false;
        }
        heads.put(signLocation, position);
        return true;
    }

    public  void  addHeads(HashMap<SignLocation, Integer> signs){
        this.heads = signs;
    }

    public boolean removeHead(SignLocation signLocation){
        for(SignLocation location : heads.keySet()){
            if(location.toString().equalsIgnoreCase(signLocation.toString())){
                heads.remove(location);
                return true;
            }
        }
        return false;
    }

}
