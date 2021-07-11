package dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
    //创建一个线程池
    private static ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler client;

    private int count=0;

    //编写方法使用代理模式，获取一个代理对象

    public Object getBean(final Class<?> serviceClass,final String providerName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{serviceClass},(proxy, method, args) -> {
            //{} 部分代码，客户端每调用一次 hello，就会进入该代码块
            System.out.println("(proxy, method, args) 进入...." + (++count) + " 次");
            if(client==null){
                initClient();
            }

            //System.out.println(args);
            //设置要发给服务器的信息
            //providerName:协议头  args[0]:客户端调用api时 hello(args[0])
            client.setPara(providerName+args[0]);

            return executorService.submit(client).get();

        });
    }

    //初始化客户端
    private static void initClient(){
        client=new NettyClientHandler();
        //创建EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(client);
                    }
                });

        try {
            bootstrap.connect("localhost",7000).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
