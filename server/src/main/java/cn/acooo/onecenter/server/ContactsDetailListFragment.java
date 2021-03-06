package cn.acooo.onecenter.server;

import android.app.ListFragment;
import android.os.Bundle;

import cn.acooo.onecenter.server.adapter.ContactsAdapter;

/**
 * Created by kthh on 14/12/26.
 */
public class ContactsDetailListFragment extends ListFragment {

    private ContactsAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (adapter == null){
            adapter = new ContactsAdapter(getActivity());
        }
        setListAdapter(adapter);
    }
}
