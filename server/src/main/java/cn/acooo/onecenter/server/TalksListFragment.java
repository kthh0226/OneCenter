package cn.acooo.onecenter.server;

import android.app.ListFragment;
import android.os.Bundle;

import cn.acooo.onecenter.server.adapter.TalksAdapter;

/**
 * Created by ly580914 on 15/1/15.
 */
public class TalksListFragment extends ListFragment{
    private TalksAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (adapter == null){
            adapter = new TalksAdapter(getActivity());
        }
        setListAdapter(adapter);
    }
}
