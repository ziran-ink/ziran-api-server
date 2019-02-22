package com.github.ziran_ink.ziran_api_server;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import com.alibaba.fastjson.JSON;
import com.github.microprograms.micro_api_runtime.annotation.MicroApi;
import com.github.microprograms.micro_api_runtime.model.Request;
import com.github.microprograms.micro_api_runtime.model.Response;
import com.github.microprograms.micro_api_runtime.utils.MicroApiUtils;
import com.github.microprograms.micro_nested_data_model_runtime.Comment;
import com.github.microprograms.micro_nested_data_model_runtime.Required;
import eu.maxschuster.dataurl.DataUrl;
import eu.maxschuster.dataurl.DataUrlBuilder;
import eu.maxschuster.dataurl.DataUrlEncoding;
import eu.maxschuster.dataurl.DataUrlSerializer;

@MicroApi(comment = "工具 - 生成图片data url", version = "v0.0.3")
public class Tools_CreatePicDataUrl_Api {

    public static void main(String[] args) throws Exception {
        Req req = new Req();
        req.setPicUrl("https://www.easyicon.net/download/png/1149253/48/");
        System.out.println(JSON.toJSONString(execute(req)));
    }

    private static void core(Req req, Resp resp) throws Exception {
        Tools_DownloadFileByAria2_Api.Req dfbaReq = new Tools_DownloadFileByAria2_Api.Req();
        dfbaReq.setUrl(req.getPicUrl());
        Tools_DownloadFileByAria2_Api.Resp dfbaResp = (Tools_DownloadFileByAria2_Api.Resp) Tools_DownloadFileByAria2_Api.execute(dfbaReq);
        File file = new File(dfbaResp.getData());
        try (FileInputStream is = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            IOUtils.readFully(is, buffer);
            DataUrl unserialized = new DataUrlBuilder().setMimeType("image/png").setEncoding(DataUrlEncoding.BASE64).setData(buffer).build();
            String serialized = new DataUrlSerializer().serialize(unserialized);
            resp.setData(serialized);
        }
    }

    public static Response execute(Request request) throws Exception {
        Req req = (Req) request;
        MicroApiUtils.throwExceptionIfBlank(req.getPicUrl(), "picUrl");
        Resp resp = new Resp();
        core(req, resp);
        return resp;
    }

    public static class Req extends Request {

        @Comment(value = "图片url")
        @Required(value = true)
        private String picUrl;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }

    public static class Resp extends Response {

        @Comment(value = "图片DataUrl")
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
