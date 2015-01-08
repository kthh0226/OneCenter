package cn.acooo.onecenter.server;

import android.app.ListFragment;
import android.os.Bundle;

import cn.acooo.onecenter.server.adapter.MyAppListAdapter;

/**
 * Created by kthh on 14/12/26.
 */
public class AppDetailListFragment extends ListFragment {
    private MyAppListAdapter myAppListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(myAppListAdapter == null){
            myAppListAdapter = new MyAppListAdapter(getActivity());
        }
        setListAdapter(myAppListAdapter);
    }
}
