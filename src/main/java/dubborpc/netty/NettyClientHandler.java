package dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context; //上下文

    private String result;//调用后返回的结果

    private String para;//客户端调用方法时传入的参数


    //与服务器连接创建成功后，就会被调用(1)
    //唤醒等待的线程
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context=ctx;//因为在其他方法会使用到ctx
    }

    //收到服务器数据后，调用方法(4)
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result=msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //被代理对象调用，发送数据给服务器，然后wait ,等待被唤醒(channelRead) ->返回结果 (3)->(5)
    @Override
    public synchronized Object call() throws Exception {

        context.writeAndFlush(para);
        //进行
        wait(); //等待获取服务器的结果后，唤醒
        return result;
    }

    //(2)
    public void setPara(String para){
        this.para=para;
    }
}
