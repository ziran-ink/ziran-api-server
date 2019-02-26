package com.github.ziran_ink.ziran_api_server;

import com.github.microprograms.micro_api_runtime.annotation.MicroApi;
import com.github.microprograms.micro_api_runtime.model.Request;
import com.github.microprograms.micro_api_runtime.model.Response;
import com.github.microprograms.micro_nested_data_model_runtime.Comment;
import com.github.microprograms.micro_nested_data_model_runtime.Required;

@MicroApi(comment = "接口 - 查询接口版本", version = "v0.0.5")
public class Api_QueryVersion_Api {

    private static void core(Request req, Resp resp) throws Exception {
        resp.setData(Api_QueryVersion_Api.class.getDeclaredAnnotationsByType(MicroApi.class)[0].version());
    }

    public static Response execute(Request request) throws Exception {
        Request req = request;
        Resp resp = new Resp();
        core(req, resp);
        return resp;
    }

    public static class Resp extends Response {

        @Comment(value = "版本")
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
