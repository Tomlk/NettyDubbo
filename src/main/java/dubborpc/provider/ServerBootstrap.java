package dubborpc.provider;

import dubborpc.netty.NettyServer;

//ServerBootstrap会启动一个服务提供者，就是NettyServer。
public class ServerBootstrap {
    public static void main(String[] args) {
        //代码待填..
        new NettyServer().startServer("localhost",6667);
    }
}
