package dubborpc.netty;

import dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        System.out.println(" msg="+msg);

        //客户端在调用服务器的API时，需要定义一个协议
        //比如我们要求每发次发消息时都必须以某个字符串开头 比如说"HelloService#hello#"
        if(msg.toString().startsWith("HelloService#hello#")){
            //System.out.println("服务器发送消息");
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
            //System.out.println("服务器发送数据:"+result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
