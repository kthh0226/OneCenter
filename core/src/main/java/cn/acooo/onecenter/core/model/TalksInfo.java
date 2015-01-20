package cn.acooo.onecenter.core.model;

import cn.acooo.onecenter.core.auto.OneCenterProtos;

/**
 * Created by ly580914 on 15/1/15.
 */
public class TalksInfo {
    String number;
    int type;
    long date;
    String name;
    long duration;
    int id;
    public TalksInfo(){};
    public TalksInfo(OneCenterProtos.TalksInfo info){
        this.number = info.getNumber();
        this.type =info.getType();
        this.date = info.getDate();
        this.name = info.getName();
        this.duration = info.getDuration();
        this.id = info.getId();
    }
    public void setNumber(String number){
        this.number = number;
    }
    public void setType(int type){
        this.type = type;
    }
    public void setDate(long date){
        this.date =date;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDuration(long duration){
        this.duration = duration;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getNumber(){
        return number;
    }
    public int getType(){
        return type;
    }
    public long getDate(){
        return date;
    }
    public  String getName(){
        return name;
    }
    public long getDuration(){
        return duration;
    }
    public int getId(){
        return id;
    }
}
