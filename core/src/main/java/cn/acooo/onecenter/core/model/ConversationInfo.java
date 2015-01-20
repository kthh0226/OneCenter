package cn.acooo.onecenter.core.model;

import cn.acooo.onecenter.core.auto.OneCenterProtos;

/**
 * Created by ly580914 on 15/1/19.
 */
public class ConversationInfo {
    private int id;
    private String name;
    private long date;
    private int count;
    private int recipient;
    private String snippet;

    public ConversationInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConversationInfo(OneCenterProtos.ConversationInfo info) {
        this.id = info.getId();
        this.name = info.getName();
        this.count = info.getCount();
        this.date = info.getDate();
        this.recipient = info.getRecipient();
        this.snippet = info.getSnippet();
    }


    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public int getRecipient() {

        return recipient;
    }

    public void setRecipient(int recipient) {
        this.recipient = recipient;
    }

    public int getCount() {

        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getDate() {

        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
