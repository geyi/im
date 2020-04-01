package com.airing.im.http;

import com.airing.im.IMApplicationTests;
import com.airing.im.enums.ResponseState;
import com.airing.im.utils.http.HttpRequestUtils;
import org.junit.jupiter.api.Test;

public class HttpTest extends IMApplicationTests {
    @Test
    public void testPost() {
        String url = "http://172.16.25.133:8888/im/chat/sendMsg";
        String jsonParams = "发送聊天消息";
        String ret = HttpRequestUtils.post(url, jsonParams, ResponseState.SEND_MSG_EXCPTION);
        System.out.println(ret);
    }
}
