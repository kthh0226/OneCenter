package cn.acooo.onecenter.model;

public class AppItem {
	private Integer icon;
	private String name;
	private String des;
	private int count;
	
	public Integer getIcon() {
		return icon;
	}
	public void setIcon(Integer icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "AppItem [icon=" + icon + ", name=" + name + ", des=" + des
				+ ", count=" + count + "]";
	}
}
