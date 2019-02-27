package com.github.ziran_ink.ziran_api_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.microprograms.micro_api_runtime.annotation.MicroApi;
import com.github.microprograms.micro_api_runtime.exception.MicroApiPassthroughException;
import com.github.microprograms.micro_api_runtime.model.Request;
import com.github.microprograms.micro_api_runtime.model.Response;
import com.github.microprograms.micro_api_runtime.utils.MicroApiUtils;
import com.github.microprograms.micro_nested_data_model_runtime.Comment;
import com.github.microprograms.micro_nested_data_model_runtime.Required;
import com.github.ziran_ink.web_weixin_sdk.core.Weixin;
import com.github.ziran_ink.ziran_api_server.utils.Tokens;

@MicroApi(comment = "微信 - 发送消息", version = "v0.0.5")
public class Weixin_SendMsg_Api {

    private static Logger log = LoggerFactory.getLogger(Weixin_SendMsg_Api.class);

    private static void core(Req req, Response resp) throws Exception {
        if (!Tokens.isValidToken(req.getToken())) {
            throw new MicroApiPassthroughException(ErrorCodeEnum.invalid_token);
        }
        Weixin weixin = Tokens.getTokenProp(req.getToken(), "weixin");
        if (weixin == null || !weixin.getStorage().isAlive()) {
            throw new MicroApiPassthroughException(ErrorCodeEnum.invalid_token);
        }
        if (!weixin.sendMsgByNickName(req.getMsg(), req.getNickname())) {
            throw new MicroApiPassthroughException(ErrorCodeEnum.weixin_send_msg_error);
        }
    }

    public static Response execute(Request request) throws Exception {
        Req req = (Req) request;
        MicroApiUtils.throwExceptionIfBlank(req.getToken(), "token");
        MicroApiUtils.throwExceptionIfBlank(req.getNickname(), "nickname");
        MicroApiUtils.throwExceptionIfBlank(req.getMsg(), "msg");
        Response resp = new Response();
        core(req, resp);
        return resp;
    }

    public static class Req extends Request {

        @Comment(value = "Token")
        @Required(value = true)
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Comment(value = "昵称")
        @Required(value = true)
        private String nickname;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        @Comment(value = "消息")
        @Required(value = true)
        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
