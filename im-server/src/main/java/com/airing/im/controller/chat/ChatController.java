package com.airing.im.controller.chat;

import com.airing.im.bean.RetDataBean;
import com.airing.im.bean.game.chat.ChatMsgBean;
import com.airing.im.bean.game.chat.ChatParamBean;
import com.airing.im.controller.BaseController;
import com.airing.im.enums.ResponseState;
import com.airing.im.service.chat.ChatService;
import com.airing.im.service.route.RouteExecutor;
import com.airing.im.wrapper.ServerCacheWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chat")
public class ChatController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;
    @Autowired
    private ServerCacheWrapper serverCacheWrapper;
    @Autowired
    private RouteExecutor routeExecutor;

    @RequestMapping("")
    public String index(Model model, String account) {
        List<String> serverList = this.serverCacheWrapper.getServerList();
        String server = this.routeExecutor.getServer(serverList, account);
        String[] info = StringUtils.split(server, ':');
        String httpServer = "http://" + info[0] + ":" + info[1] + "/im";
        String wsServer = "ws://" + info[0] + ":" + info[2] + "/im";
        model.addAttribute("account", account);
        model.addAttribute("httpServer", httpServer);
        model.addAttribute("wsServer", wsServer);
        return "chat/chat.html";
    }

    @CrossOrigin
    @RequestMapping("/refPage")
    @ResponseBody
    public RetDataBean refPage(Model model, String account, int offset, int limit) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("account", account);
            params.put("offset", offset);
            params.put("limit", limit);
            return super.successResult(this.chatService.searchChatRefPage(params));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.errorResult(ResponseState.RESULT_SYS_ERR);
        }

    }

    @CrossOrigin
    @RequestMapping("/record")
    @ResponseBody
    public RetDataBean record(Model model, ChatParamBean chat) {
        try {
            return super.successResult(this.chatService.searchChatRecord(chat));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.errorResult(ResponseState.RESULT_SYS_ERR);
        }
    }

    @RequestMapping(value = "sendMsg", method = RequestMethod.POST)
    @ResponseBody
    public RetDataBean sendMsg(@RequestBody ChatMsgBean msgBean) {
        try {
            this.chatService.sendMsg2User(msgBean);
            return super.successResult(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.errorResult(ResponseState.RESULT_SYS_ERR);
        }
    }
}
