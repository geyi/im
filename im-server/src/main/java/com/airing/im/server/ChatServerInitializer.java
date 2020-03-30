package com.airing.im.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.context.ApplicationContext;

public class ChatServerInitializer extends ChannelInitializer<Channel> {
	private final ChannelGroup group;

	public ChatServerInitializer(ChannelGroup group) {
		this.group = group;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// HttpRequestDecoder和HttpResponseEncoder的组合，使服务器端的HTTP实现更加容易
		pipeline.addLast(new HttpServerCodec());
		// 支持异步发送大的码流，但不占用过多内存，防止发生Java内存溢出的错误
		// pipeline.addLast(new ChunkedWriteHandler());
		/*
		 将HttpMessage及其后续HttpContents聚合为单个的FullHttpRequest或者FullHttpResponse
		 原因是HTTP解码器在每个HTTP消息中会生成多个消息对象
		 */
		pipeline.addLast(new HttpObjectAggregator(64 * 1024));
		// pipeline.addLast(new HttpRequestHandler("/chat/ws"));
		/*
		负责websocket握手以及控制帧（Close、Ping、Pong）的处理
		文本和二进制数据帧将传递给管道中的下一个处理程序（TextWebSocketFrameHandler）进行处理
		 */
		pipeline.addLast(new WebSocketServerProtocolHandler("/im"));
		pipeline.addLast(new Base64Decoder());
		pipeline.addLast(new Base64Encoder());
		pipeline.addLast(new TextWebSocketFrameHandler(group));
	}
}
