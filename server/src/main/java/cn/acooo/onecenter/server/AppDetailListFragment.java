package cn.acooo.onecenter.server;

import android.app.ListFragment;
import android.os.Bundle;

import cn.acooo.onecenter.server.adapter.MyAppListAdapter;

/**
 * Created by kthh on 14/12/26.
 */
public class AppDetailListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new MyAppListAdapter(getActivity()));
    }
}
