package cn.acooo.onecenter.server.ViewHolder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.acooo.onecenter.server.R;

/**
 * Created by ly580914 on 15/1/21.
 */
public class EmptyFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_page,null);
        return view;
    }
}
