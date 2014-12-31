package cn.acooo.onecenter.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kthh on 14/12/26.
 * 我的手机中的功能条目
 */
public enum PhoneMenus {
    APPS(0,"应用"),CONTACTS(1,"通讯录"),MESSAGE(2,"短信"),TALKS(3,"通话记录");

    private int index;
    private String title;

    private PhoneMenus(int index, String title){
        this.index = index;
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static List<PhoneMenus> toItems(){
        List<PhoneMenus> rs = new ArrayList<PhoneMenus>();
        for(PhoneMenus m : PhoneMenus.values()){
            rs.add(m);
        }
        return rs;
    }

    public static PhoneMenus indexOf(int index){
        return PhoneMenus.values()[index];
    }

}
