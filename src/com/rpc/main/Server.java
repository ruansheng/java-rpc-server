package com.rpc.main;

/**
 * Created by ruansheng on 16/7/1.
 */
public class Server {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new Connector().bind(port);
    }
}
