package cn.acooo.onecenter.server.async;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.acooo.onecenter.core.model.AppInfo;
import cn.acooo.onecenter.server.App;
import cn.acooo.onecenter.server.adapter.MyAppListAdapter;

/**
 * Created by kthh on 15/1/7.
 */
public class DownloadAppTask extends AsyncTask<String,Integer,Integer> {
    private MyAppListAdapter adapter;
    private AppInfo appInfo;
    private File downloadFile;
    private View view;
    public DownloadAppTask(MyAppListAdapter adapter,AppInfo appInfo,View view){
        this.adapter = adapter;
        this.appInfo = appInfo;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Integer integer) {
        appInfo.setDownloading(false);
        appInfo.setCurProgress(0L);
        adapter.notifyDataSetChanged();
        if(downloadFile != null && downloadFile.exists()){
            Intent installIntent = new Intent();
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setAction(android.content.Intent.ACTION_VIEW);
            installIntent.setDataAndType(Uri.fromFile(downloadFile),
                    "application/vnd.android.package-archive");
            view.getContext().startActivity(installIntent);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        appInfo.setCurProgress(appInfo.getCurProgress() + values[0]);

       // adapter.notifyDataSetChanged();
    }

    @Override
    protected Integer doInBackground(String... params) {
        String url = "http://"+App.selectedPhoneClient.getIp()+":9090"+appInfo.getPublicSourceDir();
        Log.i(App.TAG,url);
        Log.i(App.TAG,appInfo.getPublicSourceDir());
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try{
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                InputStream in = entity.getContent();
                byte buf[] = new byte[1024 * 1024];
                int numBytesRead;
                String filePath = App.DOWNLOAD_PATH + params[0]+".apk";
                downloadFile = new File(filePath);
                if(downloadFile.exists()){
                    downloadFile.delete();
                }
                BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(downloadFile));
                do{
                    numBytesRead = in.read(buf);
                    if(numBytesRead > 0){
                        fos.write(buf,0,numBytesRead);
                        publishProgress(numBytesRead);
                    }
                }while (numBytesRead > 0);
                fos.flush();
                fos.close();
            }
        }catch (Exception e){
            Log.e(App.TAG,"download file error",e);
            return -1;
        }finally {
            httpClient.getConnectionManager().shutdown();
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
