package cn.acooo.onecenter.core.model;

import android.graphics.Bitmap;

import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.utils.ImageUtil;

/**
 * Created by kthh on 14/12/30.
 */
public class ContactsInfo {

    public ContactsInfo(OneCenterProtos.ContactsInfo info){
        this.name = info.getName();
        this.number = info.getNumber();
        this.id = info.getId();
        this.type = info.getType();
        this.icon = ImageUtil.Bytes2Bimap(info.getIcon().toByteArray());
    }

    public ContactsInfo(){

    }


    private String number;
    private String name;
    private long id;
    /**
     * 0是存储卡的联系人，1是sim卡的联系人
     */
    private int type;

    private Bitmap icon;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }



    @Override
    public String toString() {
        return "ContactsInfo{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", type=" + type +
                ", icon=" + icon +
                '}';
    }
}
