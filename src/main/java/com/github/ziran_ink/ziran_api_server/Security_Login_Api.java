package com.github.ziran_ink.ziran_api_server;

import com.github.microprograms.micro_api_runtime.annotation.MicroApi;
import com.github.microprograms.micro_api_runtime.exception.MicroApiPassthroughException;
import com.github.microprograms.micro_api_runtime.model.Request;
import com.github.microprograms.micro_api_runtime.model.Response;
import com.github.microprograms.micro_api_runtime.utils.MicroApiUtils;
import com.github.microprograms.micro_nested_data_model_runtime.Comment;
import com.github.microprograms.micro_nested_data_model_runtime.Required;
import com.github.ziran_ink.ziran_api_server.utils.Tokens;

@MicroApi(comment = "安全 - 登录", version = "v0.0.5")
public class Security_Login_Api {

    private static void core(Req req, Resp resp) throws Exception {
        if (!"pass123".equals(req.getPassword())) {
            throw new MicroApiPassthroughException(ErrorCodeEnum.invalid_password);
        }
        resp.setData(Tokens.createToken());
    }

    public static Response execute(Request request) throws Exception {
        Req req = (Req) request;
        MicroApiUtils.throwExceptionIfBlank(req.getUsername(), "username");
        MicroApiUtils.throwExceptionIfBlank(req.getPassword(), "password");
        Resp resp = new Resp();
        core(req, resp);
        return resp;
    }

    public static class Req extends Request {

        @Comment(value = "用户名")
        @Required(value = true)
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Comment(value = "密码（pass123）")
        @Required(value = true)
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class Resp extends Response {

        @Comment(value = "Token")
        @Required(value = true)
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
