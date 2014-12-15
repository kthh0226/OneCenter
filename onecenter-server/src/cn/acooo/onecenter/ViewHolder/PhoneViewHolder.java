package cn.acooo.onecenter.ViewHolder;

import android.widget.TextView;

public class PhoneViewHolder {
	private TextView textView;

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	@Override
	public String toString() {
		return "PhoneViewHolder [textView=" + textView + "]";
	}
}
