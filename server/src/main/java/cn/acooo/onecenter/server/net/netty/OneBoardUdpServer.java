package cn.acooo.onecenter.server.net.netty;

import cn.acooo.onecenter.core.auto.OneCenterProtos;
import cn.acooo.onecenter.core.server.UDPServer;
import cn.acooo.onecenter.core.utils.UDPUtils;

/**
 * Created by kthh on 15/1/19.
 */
public class OneBoardUdpServer extends UDPServer {

    public OneBoardUdpServer(int port){
        setPort(port);
    }
    @Override
    public void searchedOneBoard(String clientIp) {
        super.searchedOneBoard(clientIp);
        UDPUtils.send(OneCenterProtos.UDPType.IS_ONEBOARD, UDPUtils.BROADCAST_IP, getPort());
    }
}
