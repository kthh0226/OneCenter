package cn.acooo.onecenter.server;

import android.app.ListFragment;
import android.os.Bundle;

import cn.acooo.onecenter.server.adapter.ConversationAdapter;
import cn.acooo.onecenter.server.adapter.SmsAdapter;

/**
 * Created by ly580914 on 15/1/15.
 */
public class ConversationListFragment extends ListFragment {
    private ConversationAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (adapter == null){
            adapter = new ConversationAdapter(getActivity());
        }
        setListAdapter(adapter);
    }
}
