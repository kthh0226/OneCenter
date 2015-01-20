package cn.acooo.onecenter.server;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cn.acooo.onecenter.server.adapter.MyAppListAdapter;

/**
 * Created by kthh on 14/12/26.
 */
public class AppDetailListFragment extends Fragment {
    private MyAppListAdapter myAppListAdapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(myAppListAdapter == null){
            myAppListAdapter = new MyAppListAdapter(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_detail, container, false);
        listView = (ListView)view.findViewById(R.id.apps);
        listView.setAdapter(myAppListAdapter);
        return view;
    }

    public MyAppListAdapter getMyAppListAdapter(){
        return this.myAppListAdapter;
    }
}
