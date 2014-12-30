package cn.acooo.onecenter.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kthh on 14/12/26.
 * 我的手机中的功能条目
 */
public class MyPhoneContent {
    public static List<PhoneItem> ITEMS = new ArrayList<PhoneItem>();
    public static Map<String,PhoneItem> ITEM_MAP = new HashMap<String,PhoneItem>();

    static {
        addItem(new PhoneItem("0","应用"));
        addItem(new PhoneItem("1","通讯录"));
        addItem(new PhoneItem("2","短信"));
        addItem(new PhoneItem("3","通话记录"));
       // addItem(new PhoneItem("4","打电话"));
    }
    public static void addItem(PhoneItem phoneItem){
        ITEMS.add(phoneItem);
        ITEM_MAP.put(phoneItem.id,phoneItem);
    }

    public static class PhoneItem{
        private String id;
        private String content;

        public PhoneItem(String id,String content){
            this.id = id;
            this.content = content;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
