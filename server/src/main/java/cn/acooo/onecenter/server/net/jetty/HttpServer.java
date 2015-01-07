package cn.acooo.onecenter.server.net.jetty;

import android.util.Log;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import cn.acooo.onecenter.server.App;

/**
 * Created by kthh on 15/1/4.
 */
public class HttpServer implements Runnable {
    private int port = 9090;
    public HttpServer(){

    }
    public HttpServer(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try{
            Server server = new Server();
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

            App.jettyServer = server;
            server.join();
        }catch (Exception e){
            Log.e(App.TAG, "jetty start error", e);
        }
    }
}
