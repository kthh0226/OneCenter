package cn.acooo.onecenter.server.async;

import android.os.AsyncTask;

import cn.acooo.onecenter.core.model.AppInfo;
import cn.acooo.onecenter.server.adapter.MyAppListAdapter;

/**
 * Created by kthh on 15/1/7.
 */
public class DownloadAppTask extends AsyncTask<String,Long,Integer> {
    private MyAppListAdapter adapter;
    private AppInfo appInfo;

    public DownloadAppTask(MyAppListAdapter adapter,AppInfo appInfo){
        this.adapter = adapter;
        this.appInfo = appInfo;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Integer integer) {
        appInfo.setDownloading(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        appInfo.setCurProgress(values[0]);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected Integer doInBackground(String... params) {
        
        return null;
    }

    @Override
    protected void onCancelled(Integer integer) {

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

}
