package com.rpc.main;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.rpc.handler.ConnectorHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by ruansheng on 16/7/1.
 */
public class ChildChannelHandler  extends ChannelInitializer<SocketChannel>{

	private Logger logger = null;
	
	public ChildChannelHandler() {
		this.logger = Logger.getLogger(ChildChannelHandler.class);
    	PropertyConfigurator.configure("res/log4j.properties");
	}
	
    @Override
    protected void initChannel(SocketChannel arg0) throws Exception {
        arg0.pipeline().addLast(new StringDecoder());
        arg0.pipeline().addLast(new ConnectorHandler());

    	this.logger.info("ChildChannelHandler:" + arg0.remoteAddress() +"连接上");
    }
}
