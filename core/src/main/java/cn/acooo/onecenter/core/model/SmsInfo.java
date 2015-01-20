package cn.acooo.onecenter.core.model;

import cn.acooo.onecenter.core.auto.OneCenterProtos;

/**
 * Created by ly580914 on 15/1/15.
 */
public class SmsInfo {
    private String name;
    private String address;
    private long date;
    private int type;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }

    private String body;
    private int id ;
    private boolean isError = false;
    public SmsInfo(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SmsInfo(OneCenterProtos.SmsInfo info){
        this.name = info.getName();

        this.address = info.getAddress();
        this.body = info.getBody();
        this.date = info.getDate();
        this.type = info.getType();
        this.id = info.getId();
    };
    public void setAddress(String address){
        this.address = address;
    }
    public void setDate(long date){
        this.date = date;
    }
    public void setType(int type){
        this.type = type;
    }
    public void setBody(String body){
        this.body = body;
    }
    public void setId(int id){ this.id = id; }
    public String getAddress(){return address;}
    public long getDate(){return date;}
    public int getType(){return type;}
    public String getBody(){return body;}
    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return address +":"+ date +":"+ type +":"+ body;
    }
}
