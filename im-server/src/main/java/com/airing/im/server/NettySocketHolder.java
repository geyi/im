package com.airing.im.server;

import com.airing.im.constant.Common;
import com.airing.im.utils.RedissonUtils;
import com.airing.im.utils.SpringBeanFactory;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class NettySocketHolder {
	private static final Logger log = LoggerFactory.getLogger(NettySocketHolder.class);
	private static Map<String, Channel> MAP = new ConcurrentHashMap<>(512);
	private static RedissonUtils redissonUtils;

	public static void put(String id, Channel socketChannel) {
		MAP.put(id, socketChannel);
	}

	public static void put(String id, Channel socketChannel, int type) {
		MAP.put(id + "_" + type, socketChannel);
		log.info("在线连接：{}", MAP.size());
	}

	public static Channel get(String id) {
		return MAP.get(id);
	}

	public static Map<String, Channel> getMAP() {
		return MAP;
	}

	public static Channel remove(String id, int type) {
		return MAP.remove(id + "_" + type);
	}

	public static String remove(Channel socketChannel) {
		if (redissonUtils == null) {
			redissonUtils = SpringBeanFactory.getBean(RedissonUtils.class);
		}

		final String[] key = new String[1];
		MAP.entrySet().stream()
			.filter(entry -> entry.getValue() == socketChannel)
			.forEach((entry) -> {
				MAP.remove(entry.getKey());
				redissonUtils.sRem(Common.REDIS_KEY_CS_LIST, entry.getKey());
				key[0] = entry.getKey();
			});
		return key[0];

		/*for (Map.Entry<String, Channel> es : MAP.entrySet()) {
			String key = es.getKey();
			Channel value = es.getValue();
			if (value == socketChannel) {
				MAP.remove(key);
				redissonUtils.sRem(Common.REDIS_KEY_CS_LIST, key);
			}
		}*/
	}

	public static boolean containsKey(String id) {
		return MAP.containsKey(id);
	}

	public static boolean containsKey(String id, int type) {
		return MAP.containsKey(id + "_" + type);
	}

	public static void sendMsg(String key, String msg) {
		if (StringUtils.isEmpty(msg)) {
			log.debug("消息内容为空，不发送");
			return;
		}
		Channel channel = MAP.get(key);
		if (channel == null) {
			log.error("未找到{}的channel", key);
			return;
		}
		channel.writeAndFlush(new TextWebSocketFrame(msg));
		log.info("给{}发送消息：{}", key, msg);
	}

	public static void sendMsg(String key, String msg, int type) {
		JSONObject jsonObject = JSONObject.parseObject(msg);
		jsonObject.put("timestamp", System.currentTimeMillis());
		String finalMsg = jsonObject.toJSONString();
		Channel channel = MAP.get(key + "_" + type);
		if (channel == null) {
			log.warn("未找到{}的channel", key);
			return;
		}
		ChannelFuture channelFuture = channel.writeAndFlush(new TextWebSocketFrame(finalMsg));
		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					log.info("给{}发送消息：{}", key, finalMsg);
				}
			}
		});
	}
}
