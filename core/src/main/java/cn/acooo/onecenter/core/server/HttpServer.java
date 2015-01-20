package cn.acooo.onecenter.core.server;

import android.util.Log;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import cn.acooo.onecenter.core.BaseActivity;


/**
 * Created by kthh on 15/1/4.
 */
public class HttpServer implements Runnable {
    private int port = 9090;
    private Server server;

    public Server getServer() {
        return server;
    }

    public HttpServer(){

    }
    public HttpServer(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try{
            server = new Server();
            SelectChannelConnector connector = new SelectChannelConnector();
            connector.setPort(port);
            server.addConnector(connector);

            ResourceHandler resource_handler = new ResourceHandler();
            resource_handler.setDirectoriesListed(true);
            resource_handler.setWelcomeFiles(new String[]{ "index.html" });
            resource_handler.setResourceBase(".");
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
            server.setHandler(handlers);
            server.start();
            Log.i("ONE","jetty server starting");
            server.join();
        }catch (Exception e){
            Log.e(BaseActivity.TAG, "jetty start error", e);
        }
    }
}
