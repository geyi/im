package com.airing.im.controller.chat;

import com.airing.im.bean.game.chat.Chat;
import com.airing.im.service.chat.ChatService;
import com.airing.im.service.route.RouteExecutor;
import com.airing.im.wrapper.ServerCacheWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chat")
public class ChatController {
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
        String[] info = server.split(":");
        String httpServer = "http://" + info[0] + ":" + info[1] + "/im";
        String wsServer = "ws://" + info[0] + ":" + info[2] + "/im";
        model.addAttribute("account", account);
        model.addAttribute("httpServer", httpServer);
        model.addAttribute("wsServer", wsServer);
        return "chat/chat.html";
    }

    @CrossOrigin
    @RequestMapping("/ref")
    @ResponseBody
    public Object ref(Model model, String searchParam) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchParam", searchParam);
        return this.chatService.searchChatRef(params);
    }

    @CrossOrigin
    @RequestMapping("/record")
    @ResponseBody
    public Object record(Model model, Chat chat) {
        return this.chatService.searchChatRecord(chat);
    }

    @CrossOrigin
    @RequestMapping("/readEvent")
    @ResponseBody
    public Object readEvent(Model model, String userId) {
        return this.chatService.updateReadStatus(userId);
    }

    @RequestMapping("/switchProcessed")
    @ResponseBody
    public Object switchProcessed(Model model, String userId, int unread) {
        return this.chatService.switchProcessed(userId, unread);
    }

    @CrossOrigin
    @RequestMapping("/refPage")
    @ResponseBody
    public Object refPage(Model model, String account, int offset, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("offset", offset);
        params.put("limit", limit);
        return this.chatService.searchChatRefPage(params);
    }
}
