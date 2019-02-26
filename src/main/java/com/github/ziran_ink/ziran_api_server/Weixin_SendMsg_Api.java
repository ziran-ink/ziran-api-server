package com.github.ziran_ink.ziran_api_server;

import com.github.microprograms.micro_api_runtime.annotation.MicroApi;
import com.github.microprograms.micro_api_runtime.model.Request;
import com.github.microprograms.micro_api_runtime.model.Response;
import com.github.microprograms.micro_api_runtime.utils.MicroApiUtils;
import com.github.microprograms.micro_nested_data_model_runtime.Comment;
import com.github.microprograms.micro_nested_data_model_runtime.Required;
import com.github.ziran_ink.itchat4j.api.MessageTools;

@MicroApi(comment = "微信 - 发送消息", version = "v0.0.5")
public class Weixin_SendMsg_Api {

    private static void core(Req req, Resp resp) throws Exception {
        resp.setData(MessageTools.sendMsgByNickName(req.getNickname(), req.getMsg()) ? 1 : 0);
    }

    public static Response execute(Request request) throws Exception {
        Req req = (Req) request;
        MicroApiUtils.throwExceptionIfBlank(req.getNickname(), "nickname");
        MicroApiUtils.throwExceptionIfBlank(req.getMsg(), "msg");
        Resp resp = new Resp();
        core(req, resp);
        return resp;
    }

    public static class Req extends Request {

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

    public static class Resp extends Response {

        @Comment(value = "是否成功（0否1是）")
        @Required(value = true)
        private Integer data;

        public Integer getData() {
            return data;
        }

        public void setData(Integer data) {
            this.data = data;
        }
    }
}
