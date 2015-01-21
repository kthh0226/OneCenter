package cn.acooo.onecenter.server.adapter;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;

/**
 * Created by ly580914 on 15/1/21.
 */
public class AdapterAnimation {
    public static void setAnimation(final int position, View view, final MyBaseAdapter adapter) {
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);

        ta.setDuration(500);
        view.startAnimation(ta);

        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                adapter.getDatas().remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
