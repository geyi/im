var friends = {
  list: document.querySelector('ul.people'),
  all: document.querySelectorAll('.left .person'),
  name: ''
},

chat = {
  container: document.querySelector('.container .right .chat-content'),
  current: null,
  person: null,
  name: document.querySelector('.container .right .top .name')
};


var WS = {};
WS.URL = $('#chatUrl').val();
WS.switchFlag = false;
WS.refPage = $('#httpUrl').val() + '/chat/refPage';
WS.record = $('#httpUrl').val() + '/chat/record';
WS.readEvent = $('#httpUrl').val() + '/chat/readEvent';

WS.COMMON = (function() {
    var o = {
        init: function() {
            o.friendsClick();
            o.initFriends();
            o.initSwitch();
            // o.initProcessed();
            o.initTime();
            o.searchFriends();
            o.historyClick();
            o.initDateFormat();
            o.initEmoji();
        },

        // 组装聊天窗口
        chatWin: function(id, name) {
            $('#username').text(name);
            var html = [];
            html.push('<div class="chat-content" style="height: 400px; display: none">');
            html.push('    <div id="' + id + '">');
            html.push('        <div class="chat" data-chat="' + id + '"></div>');
            html.push('    </div>');
            html.push('</div>');
            return html.join('');
        },

        // 组装好友元素
        friend: function(isActive, id, name, hasRed, content, userAutoId, chatDate) {
            var html = [];
            if (isActive) {
                html.push('<li class="person active" data-chat="' + id + '" data-autoid="' + userAutoId + '">');
            } else {
                html.push('<li class="person" data-chat="' + id + '" data-autoid="' + userAutoId + '">');
            }
            html.push('    <img src="chat/img/user_default.jpg">');
            html.push('    <span class="name">' + name + '</span>');
            if (hasRed) {
                html.push('    <span class="time"><i class="im-tip"></i></span>');
            } else {
                html.push('    <span class="time"></span>');
            }
            html.push('    <span class="preview">' + chatDate + '</span>');
            html.push('    <span class="preview">' + o.contentCheck(content) + '</span>');
            html.push('</li>');
            return html.join('');
        },

        // 组装聊天内容
        chatContent: function(isMe, chatType, content, chatTime, senderName) {
            content = content.replace(/"emoj\/emoj-/g, '"../static/modular/cs/im/emoj/emoj-');
            var html = [];
            if (isMe) {
                // 不显示聊天时间
                // html.push('<span class="me-time">' + senderName + ' ' +  chatTime + '</span>');
                html.push('<div class="bubble me">');
            } else {
                // 不显示聊天时间
                // html.push('<span class="you-time">' + chatTime + '</span>');
                html.push('<div class="bubble you">');
            }
            html.push(content);
            html.push('</div>');

            return html.join('');
        },

        // 历史聊天内容
        hChatContent: function(isMe, chatType, content, chatTime, username) {
            content = content.replace(/"emoj\/emoj-/g, '"../static/modular/cs/im/emoj/emoj-');
            var html = [];
            if (isMe) {
                html.push('<span class="you-time">' + username + ' ' + chatTime + '</span>');
                html.push('<div class="bubble me">');
            } else {
                html.push('<span class="you-time">' + username + ' ' + chatTime + '</span>');
                html.push('<div class="bubble you">');
            }
            html.push(content);
            html.push('</div>');

            return html.join('');
        },

        // 点击好友列表切换聊天窗口
        friendsClick: function () {
            $('#friends').on('click', 'li', function () {
                // 移除红点
                // $(this).find('.time').children().remove();
                // 判断是否有小红点
                var unread = $(this).find('.time').children().size() == 1 ? true : false;
                // 移除历史记录
                $('#historyContent').html('');

                var id = $(this).data('chat');
                if ($('#' + id).length < 1) {
                    var name = $(this).find('.name').text();
                    var chatWin = o.chatWin(id, name);
                    $('#top').after(chatWin);
                    $('#' + id).slimScroll({
                        height: '400px'
                    });
                } else {
                    $('#' + id).children().children().remove();
                }

                var queryData = {};
                queryData['userId'] = id;
                queryData['offset'] = 0;
                queryData['limit'] = 51;

                var ajax = new $ax(WS.record, function (data) {
                    if (data != null) {
                        console.log(data);
                        var records = data.data;
                        var len = records.length >= 50 ? 50 : records.length;
                        for (var i = 0; i < len; i++) {
                            var record = records[i];
                            var chatContent = record.chatContent;
                            var senderId = record.senderId;
                            var senderName = record.senderName;
                            var chatTime = record.chatTime;

                            // 向窗口中添加聊天消息
                            var isMe;
                            if (WS.account == senderId) {
                                isMe = true;
                            } else {
                                isMe = false;
                            }
                            var chatContent = o.chatContent(isMe, 0, chatContent, chatTime, senderName);
                            var chatWinEle = $('.chat[data-chat=' + id + ']');
                            $(chatContent).prependTo(chatWinEle);
                        }

                        if (records.length > 50) {
                            o.historyMsgClick(id);
                        }

                        $(chatWinEle).find('p').css({
                            'word-wrap': 'break-word',
                            'max-width': '300px'
                        });
                        $(chatWinEle).find('img').css({
                            'max-width': '200px',
                            'height': 'auto;'
                        });
                    }
                }, function () {
                    console.log('初始化好友列表失败');
                })

                ajax.setData(queryData);
                ajax.start();

                $(this).hasClass('active') || o.setActive(this, id, unread);
            });
        },

        // 好友设置为选中状态
        setActive: function (f, id, unread) {
            $('#friends').find('li').filter(".active").removeClass('active');
            $(f).addClass('active');
            // 移除红点
            $(f).find('.time').children().remove();

            // $('.active-chat').parent().parent().parent().hide();
            $('.active-chat').parent().parent().parent().remove();
            $('.active-chat').removeClass('active-chat');

            $('.chat[data-chat=' + id + ']').parent().parent().parent().show();
            $('.chat[data-chat=' + id + ']').addClass('active-chat');

            // 设置用户名字
            friends.name = f.querySelector('.name').innerText;
            chat.name.innerHTML = friends.name;
            // 设置用户id
            $('#userAutoId').text($(f).data('autoid'));

            // 已读事件
            // WS.CHAT.readEvent(id);

            /*if ((unread && WS.processed.isChecked()) || (!unread && !WS.processed.isChecked())) {
                WS.processed.setPosition(true);
            }*/

            o.scrollBottom(id);
        },

        // 重置编辑器的大小
        resetEdit: function () {
            var h = parseFloat($('.right').height());
            var nh = h - 480;
            $('#msg').css({
                'min-height': nh,
                'max-height': nh,
                'padding-left': '5px'
            });
        },

        // 纠正emoji弹框的位置
        resetEmoji: function () {
            var so = $('.icon-xiaolian').offset();
            var sTop = so.top;
            var sLeft = so.left;

            var top = sTop - 244.5;
            var left = sLeft - 126.5;

            $('.popover').css({
                'top': top + 'px',
                'left': left + 'px'
            });
        },

        // emoji表情点击事件
        emojiClick: function () {
            $('.emoji-box').on('click', 'img', function () {
                var num = $(this).attr('id').replace('emoji', '');

                var msg = $('#msg').html();
                msg = msg + '<img src="chat/img/emoj/emoj-' + num + '.png" class="emoji-img">';

                $('#msg').html(msg);
            });
        },
        
        // 聊天内容检测
        contentCheck: function (chatContent) {
            var img = /\<img/.test(chatContent);
            var emoji = /emoj/.test(chatContent);
            var div = /\<div/.test(chatContent);

            if (emoji) {
                return 'emoji';
            } else if (img) {
                return 'image';
            } else if (div) {
                chatContent = chatContent.replace(/\<div\>/g, '');
                chatContent = chatContent.replace(/\<\/div\>/g, '');
                chatContent = chatContent.replace(/\<br\>/g, '');
                return chatContent;
            } else {
                return chatContent;
            }
        },

        // 暂时无用
        resetFriends: function () {
            var leftH = $('.left').height();
            var precent = parseFloat(100 - 4700 / leftH).toFixed(2);
            $('#friends').slimScroll({
                height: precent + '%'
            });
        },

        // 进入聊天页面初始化好友列表
        initFriends: function () {
            var queryData = {};
            queryData['account'] = $('#account').val();
            queryData['offset'] = 0;
            queryData['limit'] = 10;

            var ajax = new $ax(WS.refPage, function (data) {
                if (data != null) {
                    console.log(data);
                    var friends = data.data;
                    for (var i = 0, len = friends.length; i < len; i++) {
                        var friend = friends[i];
                        var userId = friend.userId;
                        var userAutoId = friend.userAutoId;
                        var userName = friend.userName;
                        var chatContent = friend.chatContent;
                        var chatDate = friend.chatDate;
                        var hasRed = friend.unread == 1 ? true : false;
                        var friendEle = o.friend(false, userId, userName, hasRed, chatContent, userAutoId, chatDate);
                        $(friendEle).appendTo('#friends');
                    }
                }
            }, function () {
                console.log('初始化好友列表失败');
            })

            ajax.setData(queryData);
            ajax.start();
        },

        searchFriends: function () {
            $('.glyphicon-search').on('click', function() {
                var queryData = {};
                queryData['searchParam'] = $('#searchParam').val();

                var ajax = new $ax(Feng.ctxPath + "/cs/im/ref", function (data) {
                    if (data != null) {
                        console.log(data);
                        $('#friends').html('');
                        var friends = data.data;
                        for (var i = 0, len = friends.length; i < len; i++) {
                            var friend = friends[i];
                            var userId = friend.userId;
                            var userAutoId = friend.userAutoId;
                            var userName = friend.userName;
                            var chatContent = friend.chatContent;
                            var chatDate = friend.chatDate;
                            var hasRed = friend.unread == 1 ? true : false;
                            var friendEle = o.friend(false, userId, userName, hasRed, chatContent, userAutoId, chatDate);
                            $(friendEle).prependTo('#friends');
                        }
                        // 默认打开第一个用户的对话框
                        $('#friends li').first().click();
                    }
                }, function () {
                    console.log('初始化好友列表失败');
                })

                ajax.setData(queryData);
                ajax.start();
            });

            $("#searchParam").keydown(function ($event) {
                var keycode = window.event ? $event.keyCode : $event.which;
                if (keycode == 13) {
                    $('.glyphicon-search').click();
                    return true;
                }
            });
        },

        // 聊天窗口滚动到底部
        scrollBottom: function (id) {
            // 自动滚到底部
            // console.log($('.chat[data-chat="' + id + '"]').height())
            // $('#' + id).scrollTop($('.chat[data-chat="' + id + '"]').height());
            var height = $('.chat[data-chat="' + id + '"]').height();
            console.log(height);
            $('#' + id).slimScroll({ scrollTo: height + 'px' });
        },

        // 初始化switch
        initSwitch: function () {
            var elem = document.querySelector('.js-switch');
            var init = new Switchery(elem, { size: 'small' });
            WS.switchObj = init;
        },

        initProcessed: function () {
            var elem = document.querySelector('#processed');
            var init = new Switchery(elem, { size: 'small' });
            WS.processed = init;
            elem.onchange = function () {
                console.log(elem.checked);
                var unread = 0;
                if (elem.checked) {
                    unread = 0;
                    $('#friends').find('li').filter('.active').find('span').filter('.time').html('');
                } else {
                    unread = 1;
                    $('#friends').find('li').filter('.active').find('span').filter('.time').html('<i class="im-tip"></i>');
                }
                var queryData = {};
                queryData['userId'] = $('.active-chat').data('chat');
                queryData['unread'] = unread;

                var ajax = new $ax(Feng.ctxPath + "/cs/im/switchProcessed", function (data) {
                    WS.CHAT.readEvent2(queryData['userId'], unread);
                }, function () {
                })

                ajax.setData(queryData);
                ajax.start();
            }
        },

        // 初始化时间插件
        initTime: function () {
            laydate.render({
                elem: '#chatTime',
                format: 'yyyy-MM-dd',
                trigger: 'click',
                eventElem: '.glyphicon-calendar',
                done: function (value, date, endDate) {
                    if (!o.activeCheck()) {
                        return;
                    }

                    var queryData = {};
                    queryData['userId'] = $('.active-chat').data('chat');
                    queryData['chatTime'] = value;

                    var ajax = new $ax(WS.record, function (data) {
                        if (data != null) {
                            $('#historyContent').html('');

                            console.log(data);
                            var records = data.data;
                            var username;
                            for (var i = 0, len = records.length; i < len; i++) {
                                var record = records[i];
                                var chatContent = record.chatContent;
                                var senderId = record.senderId;
                                var chatTime = record.chatTime;

                                // 向窗口中添加聊天消息
                                var isMe;
                                if (WS.account == senderId) {
                                    isMe = true;
                                    username = WS.account;
                                } else {
                                    isMe = false;
                                    username = $('#username').text();
                                }
                                var chatContent = WS.COMMON.hChatContent(isMe, 0, chatContent, chatTime, username);
                                var chatWinEle = $('#historyContent');
                                $(chatContent).prependTo(chatWinEle);
                            }

                            chatWinEle.find('p').css({
                                'word-wrap': 'break-word',
                                'max-width': '300px'
                            });
                            chatWinEle.find('img').css({
                                'max-width': '200px',
                                'height': 'auto;'
                            });

                            $('#historyDiv').slimScroll({ scrollTo: '0px' });
                        }
                    }, function () {
                        console.log('初始化好友列表失败');
                    });

                    ajax.setData(queryData);
                    ajax.start();
                }
            });
        },

        historyMsgClick: function (id) {
            $('<div class="conversation-start text-center history-msg" data-id="' + id + '"><a>More message, please check history</a></div>')
                .prependTo('.chat[data-chat=' + id + ']');

            $('.history-msg[data-id=' + id + ']').on('click', function () {
                var isHide = $('.history').is(":hidden");
                if (isHide) {
                    $('.right').css({
                        'width': '49.92%'
                    });
                    $('.history').show();
                }

                var id = $(this).data('id');
                console.log('id' + id);

                var queryData = {};
                queryData['userId'] = id;

                var ajax = new $ax(WS.record, function (data) {
                    if (data != null) {
                        $('#historyContent').html('');

                        console.log(data);
                        var records = data.data;
                        var username;
                        for (var i = 0, len = records.length; i < len; i++) {
                            var record = records[i];
                            var chatContent = record.chatContent;
                            var senderId = record.senderId;
                            var chatTime = record.chatTime;

                            // 向窗口中添加聊天消息
                            var isMe;
                            if (WS.account == senderId) {
                                isMe = true;
                                username = WS.account;
                            } else {
                                isMe = false;
                                username = $('#username').text();
                            }
                            var chatContent = WS.COMMON.hChatContent(isMe, 0, chatContent, chatTime, username);
                            var chatWinEle = $('#historyContent');
                            $(chatContent).prependTo(chatWinEle);
                        }

                        chatWinEle.find('p').css({
                            'word-wrap': 'break-word',
                            'max-width': '300px'
                        });
                        chatWinEle.find('img').css({
                            'max-width': '200px',
                            'height': 'auto;'
                        });
                    }
                }, function () {
                    console.log('初始化好友列表失败');
                })

                ajax.setData(queryData);
                ajax.start();
            });
        },

        historyClick: function () {
            $('.glyphicon-time').click(function() {
                var isHide = $('.history').is(":hidden");
                if (isHide) {
                    // $('.right').css({
                    //     'width': '49.92%'
                    // });
                    $('.right').animate({'width':'49.92%'},"slow");
                    $('.history').show();
                } else if (!isHide && $('#historyContent').children().length > 0) {
                    $('.history').hide();
                    // $('.right').css({
                    //     'width': '79.92%'
                    // });
                    $('.right').animate({'width':'79.92%'},"slow");
                    return;
                }

                if (!o.activeCheck()) {
                    return;
                }

                var id = $('.active-chat').data('chat');
                console.log('id' + id);

                var queryData = {};
                queryData['userId'] = id;

                var ajax = new $ax(WS.record, function (data) {
                    if (data != null) {
                        $('#historyContent').html('');

                        console.log(data);
                        var records = data.data;
                        var username;
                        for (var i = 0, len = records.length; i < len; i++) {
                            var record = records[i];
                            var chatContent = record.chatContent;
                            var senderId = record.senderId;
                            var chatTime = record.chatTime;

                            // 向窗口中添加聊天消息
                            var isMe;
                            if (WS.account == senderId) {
                                isMe = true;
                                username = WS.account;
                            } else {
                                isMe = false;
                                username = $('#username').text();
                            }
                            var chatContent = WS.COMMON.hChatContent(isMe, 0, chatContent, chatTime, username);
                            var chatWinEle = $('#historyContent');
                            $(chatContent).prependTo(chatWinEle);
                        }

                        chatWinEle.find('p').css({
                            'word-wrap': 'break-word',
                            'max-width': '300px'
                        });
                        chatWinEle.find('img').css({
                            'max-width': '200px',
                            'height': 'auto;'
                        });
                    }
                }, function () {
                    console.log('初始化好友列表失败');
                })

                ajax.setData(queryData);
                ajax.start();
            });
        },

        activeCheck: function () {
            if ($('.people').find('li').filter('.active').length < 1) {
                return false;
            }
            return true;
        },

        initDateFormat: function () {
            Date.prototype.Format = function (fmt) {
                var o = {
                    "M+": this.getMonth() + 1, //月份
                    "d+": this.getDate(), //日
                    "H+": this.getHours(), //小时
                    "m+": this.getMinutes(), //分
                    "s+": this.getSeconds(), //秒
                    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                    "S": this.getMilliseconds() //毫秒
                };
                if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
                for (var k in o)
                    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                return fmt;
            }
        },

        validFileType: function (filePath, regexp) {
            if (regexp instanceof RegExp) {
                var tail = filePath.substr(filePath.lastIndexOf(".") + 1);
                if (tail.match(regexp)) {
                    return true;
                }
            }
            return false;
        },
        
        readEvent: function (userId) {
            var queryData = {};
            queryData['userId'] = userId;

            var ajax = new $ax(WS.readEvent, function (data) {
            }, function () {
            })

            ajax.setData(queryData);
            ajax.start();
        },

        initEmoji: function () {
            var html = [];
            for (var i = 0; i < 6; i++) {
                html.push('<div class="emoji-box">');
                for (var j = 1; j <= 8; j++) {
                    var num = i * 8 + j;
                    html.push('<div><img src="chat/img/emoj/emoj-' + num + '.png" id="emoji' + num + '"></div>');
                }
                html.push('</div>');
            }
            $("[data-toggle='popover']").data('content', html.join(''));
        }
    };

    return {
        init: o.init,
        chatWin: o.chatWin,
        friend: o.friend,
        chatContent: o.chatContent,
        resetEdit: o.resetEdit,
        resetEmoji: o.resetEmoji,
        emojiClick: o.emojiClick,
        resetFriends: o.resetFriends,
        hChatContent: o.hChatContent,
        activeCheck: o.activeCheck,
        validFileType: o.validFileType,
        readEvent: o.readEvent
    }
}());

WS.CHAT = (function() {
    var ws = null;

    var o = {
        init: function () {
            // o.connect();
            o.send();
            o.uploadSend();
            o.switchStatus();
            o.msgKeyDown();
            o.heartbeat();
        },

        connect: function () {
            if (ws == null || ws.readyState != 1) {
                ws = new WebSocket(WS.URL);

                ws.onerror = function (e) {
                    console.log('Error : ' + e.message);
                    WS.switchFlag = false;
                }

                ws.onopen = function (data) {
                    console.log(new Date());
                    console.log('connected');
                    var account = $('#account').val();
                    var userObj = {
                        event: 1, // 1表示客服上线消息类型
                        account: account
                    };
                    ws.send(JSON.stringify(userObj));

                    WS.switchFlag = false;

                    // parent.layer.msg('online');

                    $('#friends').find('li').filter(".active").click();
                }

                ws.onclose = function () {
                    console.log(new Date());
                    console.log('disconnected');

                    if (!WS.switchFlag) {
                        WS.switchObj.setPosition(true);
                    }

                    // parent.layer.msg('offline');
                }

                ws.onmessage = function (d) {
                    if (d.data == '') {
                        return;
                    }
                    var msg = JSON.parse(d.data);
                    console.log(msg);

                    var event = msg.event;
                    if (event == 4) {
                        o.receiveReadEvent(msg);
                        return;
                    }

                    var id;
                    var isMe;
                    if (WS.account == msg.senderId) {
                        id = msg.receiverId;
                        isMe = true;
                    } else {
                        id = msg.senderId;
                        isMe = false;
                    }
                    var name = msg.userName;
                    var content = msg.chatContent;
                    var chatTime = new Date(msg.chatTime).Format("yyyy-MM-dd HH:mm:ss");
                    var userAutoId = msg.userAutoId;

                    var friendEle = $('.person[data-chat=' + id + ']');
                    if (friendEle.length > 0) {
                        if (friendEle.hasClass('active')) { // true表示接收到当前窗口用户发送的消息
                            // 将当前用户移至好友列表的第一位
                            friendEle.remove();
                            var friendNewEle = WS.COMMON.friend(true, id, name, false, content, userAutoId, chatTime);
                            $(friendNewEle).prependTo('#friends');
                            // 读事件
                            // o.readEvent(id);
                        } else { // false表示接收到不是当前窗口用户发送的消息
                            // 将发送消息的用户移至好友列表的第一位，并显示红点
                            friendEle.remove();
                            var friendNewEle = WS.COMMON.friend(false, id, name, true, content, userAutoId, chatTime);
                            $(friendNewEle).prependTo('#friends');
                        }
                    } else {
                        // 创建friend放到好友列表的第一位，并显示红点
                        var friendNewEle = WS.COMMON.friend(false, id, name, true, content, userAutoId, chatTime);
                        $(friendNewEle).prependTo('#friends');

                        // 创建聊天窗口
                        var chatWin = WS.COMMON.chatWin(id, name);
                        $('#top').after(chatWin);
                        $('#' + id).slimScroll({
                            height: '400px'
                        });
                    }

                    // 向窗口中添加聊天消息
                    var chatType = msg.chatType;
                    var senderName = msg.senderName;
                    var chatContent = WS.COMMON.chatContent(isMe, chatType, content, chatTime, senderName);
                    var chatWinEle = $('.chat[data-chat=' + id + ']');
                    $(chatContent).appendTo(chatWinEle);
                    chatWinEle.find('p').css({
                        'word-wrap': 'break-word',
                        'max-width': '300px'
                    });
                    chatWinEle.find('img').css({
                        'max-width': '200px',
                        'height': 'auto;'
                    });

                    // 自动滚到底部
                    var height = $('.chat[data-chat="' + id + '"]').height();
                    $('#' + id).slimScroll({ scrollTo: height + 'px' });
                }
            } else {
                console.log('closing connection');
                ws.close();
            }
        },

        // 发送输入内容
        send: function () {
            $('#send').click(function () {
                if (!o.wsCheck() || !WS.COMMON.activeCheck()) {
                    return;
                }

                var msg = $('#msg').html();
                $('#msg').html('');
                var activeLi = $('#friends').find('li').filter(".active");
                var msgObj = {
                    account: WS.account,
                    senderId: WS.account,
                    senderName: $('#account').val(),
                    userName: activeLi.find('.name').text(),
                    chatContent: msg,
                    chatType: 1,
                    event: 3,
                    receiverId: activeLi.data('chat') + '',
                    chatTime: new Date().getTime(),
                    userAutoId: activeLi.data('autoid')
                };
                ws.send(JSON.stringify(msgObj));

                $('#msg').focus();
            });
        },

        // 发送图片
        uploadSend: function () {
            $('#file').on('change', function () {
                if (!o.wsCheck() || !WS.COMMON.activeCheck()) {
                    return;
                }

                var file = document.getElementById("file").files[0];
                var fileName = file.name;
                var fileLength = file.size;
                var validResult = WS.COMMON.validFileType(fileName, /(png)|(jpg)|(jpeg)/i);
                if (!validResult) {
                    // parent.layer.msg('错误的文件类型！');
                    return;
                }
                if (fileLength > 5242880) {
                    // parent.layer.msg('文件大小超过5M！');
                    return;
                }

                var formData = new FormData();
                formData.append("file", file);
                $.ajax({
                    url: '../common/upload/resource',
                    type: 'post',
                    data: formData,
                    contentType: false,
                    processData: false,
                    dataType: 'json',
                    success: function(result) {
                        console.log(result);
                        var imgUrl = result.data[0];

                        var activeLi = $('#friends').find('li').filter(".active");
                        var msgObj = {
                            account: WS.account,
                            senderId: WS.account,
                            senderName: $('#account').val(),
                            userName: activeLi.find('.name').text(),
                            chatContent: '<img src="' + imgUrl + '" class="upload-img">',
                            chatType: 2,
                            event: 3,
                            receiverId: activeLi.data('chat') + '',
                            chatTime: new Date().getTime(),
                            userAutoId: activeLi.data('autoid')
                        };
                        ws.send(JSON.stringify(msgObj));

                        $('#msg').focus();
                    }
                });

                $(this).val('');
            });
        },
        
        // 在线离线切换
        switchStatus: function () {
            var elem = document.querySelector('.js-switch');

            elem.onchange = function() {
                o.connect();

                WS.switchFlag = true;
            };
        },

        wsCheck: function () {
            if (ws == null || ws.readyState != 1) {
                return false;
            }
            return true;
        },

        msgKeyDown: function () {
            $("#msg").keydown(function ($event) {
                var keycode = window.event ? $event.keyCode : $event.which;
                var evt = $event || window.event;
                // 回车-->发送消息
                if (keycode == 13 && !(evt.ctrlKey)) {
                    $event.preventDefault();
                    // 发送消息的代码
                    $('#send').click();
                    return true;
                }
                // ctrl+回车-->换行
                if (evt.ctrlKey && evt.keyCode == 13) {
                    $("#msg").append("<div><br/></div>");
                    var o = document.getElementById("msg").lastChild;
                    var textbox = document.getElementById('msg');
                    var sel = window.getSelection();
                    var range = document.createRange();
                    range.selectNodeContents(textbox);
                    range.collapse(false);
                    range.setEndAfter(o);
                    range.setStartAfter(o);
                    sel.removeAllRanges();
                    sel.addRange(range);
                    return true;
                }
            });
        },

        heartbeat: function () {
            if (WS._hbTimer != null) {
                clearTimeout(WS._hbTimer);
            }
            WS._hbTimer = setTimeout(function () {
                if (o.wsCheck()) {
                    ws.send('{}');
                    console.log("onHeartbeat")
                } else {
                    console.log("no connection");
                }
                o.heartbeat();
            }, 30000);
        },
        
        readEvent: function (userId) {
            if (o.wsCheck()) {
                var msgObj = {
                    event: 4,
                    userId: userId + '',
                    unread: 0
                };
                ws.send(JSON.stringify(msgObj));
                WS.COMMON.readEvent(userId);
            } else {
                WS.COMMON.readEvent(userId);
            }
        },

        readEvent2: function (userId, unread) {
            if (o.wsCheck()) {
                var msgObj = {
                    event: 4,
                    userId: userId + '',
                    unread: unread
                };
                ws.send(JSON.stringify(msgObj));
            }
        },

        receiveReadEvent: function (msg) {
            var userId = msg.userId;
            var unread = msg.unread;
            if (unread == 1) {
                $('.person[data-chat=' + userId + ']').find('.time').html('<i class="im-tip"></i>');
            } else if (unread == 0) {
                var friendEle = $('.person[data-chat=' + userId + ']').find('.time').children();
                if (friendEle.length > 0) {
                    friendEle.remove();
                }
            }

            var curUserId = $('.active-chat').data('chat');
            if (userId == curUserId) {
                if ((unread && WS.processed.isChecked()) || (!unread && !WS.processed.isChecked())) {
                    WS.processed.setPosition(true);
                }
            }
        }
    };

    return {
        init: o.init,
        readEvent: o.readEvent,
        readEvent2: o.readEvent2
    }
}());

$(document).ready(function () {
    WS.account = $('#account').val();
    WS.CHAT.init();
    WS.COMMON.init();

    $('#chatDiv').slimScroll({
        // height: '400px'
        height: '400px'
    });

    var leftH = $('.left').height();
    var precent = parseFloat(100 - 8400 / leftH).toFixed(2);
    $('#friends').slimScroll({
        height: precent + '%'
    });
    $('#friends').slimScroll().bind('slimscroll', function (e, pos) {
        // console.log("Reached " + pos);
        if ($('#isEnd').val() == 'true') {
            return;
        }

        var page = parseInt($('#friendListPage').val()) + 1;
        var queryData = {};
        queryData['offset'] = page * 10;
        queryData['limit'] = 10;

        var ajax = new $ax(WS.refPage, function (data) {
            if (data != null) {
                var friends = data.data;
                console.log(friends);
                var i = 0;
                var len = friends.length;
                for (; i < len; i++) {
                    var friend = friends[i];
                    var userId = friend.userId;
                    var userAutoId = friend.userAutoId;
                    var userName = friend.userName;
                    var chatContent = friend.chatContent;
                    var chatDate = friend.chatDate;
                    var hasRed = friend.unread == 1 ? true : false;
                    var friendEle = WS.COMMON.friend(false, userId, userName, hasRed, chatContent, userAutoId, chatDate);
                    $(friendEle).appendTo('#friends');
                }
                if (i == 10) {
                    $('#friendListPage').val(page);
                } else {
                    $('#isEnd').val('true');
                }
            }
        }, function () {
            console.log('初始化好友列表失败');
        })

        ajax.setData(queryData);
        ajax.start();
    });

    WS.COMMON.resetEdit();

    // 向元素集合附加弹出框句柄
    $("[data-toggle='popover']").popover();

    // 当弹出框对用户可见时触发该事件
    $("[data-toggle='popover']").on('shown.bs.popover', function () {
        WS.COMMON.resetEmoji();
        WS.COMMON.emojiClick();
    })

    $('#historyDiv').slimScroll({
        height: 100 + '%'
    });

    // 默认打开第一个用户的对话框
    $('#friends li').first().click();
});

// 监听窗口变化事件
window.onresize = function () {
    WS.COMMON.resetEdit();
    WS.COMMON.resetEmoji();
}