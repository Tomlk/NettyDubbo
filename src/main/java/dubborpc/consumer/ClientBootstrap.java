package dubborpc.consumer;

import dubborpc.netty.NettyClient;
import dubborpc.publicinterface.HelloService;

import java.util.concurrent.TimeUnit;

public class ClientBootstrap {

    //定义协议头
    public static final String providerName="HelloService#hello#";

    public static void main(String[] args) throws InterruptedException {
        //创建一个消费者
        NettyClient consumer = new NettyClient();

        //创建一个代理对象
        HelloService service =(HelloService) consumer.getBean(HelloService.class, providerName);

        //System.out.println(service);
        //通过代理对象调用服务提供者的方法(服务)
//        String res = service.hello("你好 dubbo~");
//        System.out.println("调用的结果 res= " + res);
        for(;;) {
            TimeUnit.SECONDS.sleep(2);
            //通过代理对象调用服务提供者的方法(服务)
            String res = service.hello("你好 dubbo~");//真正调用的时机
            System.out.println("调用的结果 res= " + res);
        }

    }
}
