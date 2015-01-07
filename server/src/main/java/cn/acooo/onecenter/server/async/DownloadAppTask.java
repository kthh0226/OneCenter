package cn.acooo.onecenter.server.async;

import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;

import cn.acooo.onecenter.core.model.AppInfo;
import cn.acooo.onecenter.server.adapter.MyAppListAdapter;

/**
 * Created by kthh on 15/1/7.
 */
public class DownloadAppTask extends AsyncTask<String,Integer,Integer> {
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
    protected void onProgressUpdate(Integer... values) {
        appInfo.setCurProgress(values[0]);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected Integer doInBackground(String... params) {
        String packageName = params[0];
        for(int i = 0;i<100;i++){
            publishProgress(i);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    @Override
    protected void onCancelled(Integer integer) {

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

}
