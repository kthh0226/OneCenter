package cn.acooo.onecenter.server.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by ly580914 on 15/1/20.
 */
public class ConversationItemView extends LinearLayout{
    public ConversationItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ConversationItemView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public ConversationItemView(Context context) {

        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
