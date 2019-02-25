package com.github.ziran_ink.ziran_api_server;

import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSON;
import com.github.microprograms.micro_api_runtime.annotation.MicroApi;
import com.github.microprograms.micro_api_runtime.model.Request;
import com.github.microprograms.micro_api_runtime.model.Response;
import com.github.microprograms.micro_api_runtime.utils.MicroApiUtils;
import com.github.microprograms.micro_nested_data_model_runtime.Comment;
import com.github.microprograms.micro_nested_data_model_runtime.Required;

@MicroApi(comment = "工具 - 通过aria2下载文件到指定文件夹", version = "v0.0.4")
public class Tools_DownloadFileByAria2_Api {

    private static final String default_aria2cPath = "/usr/local/bin/";

    public static void main(String[] args) throws Exception {
        Req req = new Req();
        req.setDir("/Users/xuzewei/Downloads");
        req.setUrl("https://www.easyicon.net/download/png/1149253/48/");
        System.out.println(JSON.toJSONString(execute(req)));
    }

    private static void core(Req req, Resp resp) throws Exception {
        Tools_ExecuteCommandByBash_Api.Req ecbbReq = new Tools_ExecuteCommandByBash_Api.Req();
        ecbbReq.setPathEnvironment(StringUtils.isBlank(req.getAria2cPath()) ? default_aria2cPath : req.getAria2cPath());
        if (StringUtils.isNotBlank(req.getDir())) {
            ecbbReq.setCommand(String.format("aria2c --dir=%s %s", req.getDir(), req.getUrl()));
        } else {
            ecbbReq.setCommand(String.format("aria2c %s", req.getUrl()));
        }
        Tools_ExecuteCommandByBash_Api.Resp ecbbResp = (Tools_ExecuteCommandByBash_Api.Resp) Tools_ExecuteCommandByBash_Api.execute(ecbbReq);
        resp.setData(parseFileName(ecbbResp.getCommandOutput()));
    }

    private static String parseFileName(String commandOutput) {
        /*
		 * 02/22 21:29:39 [NOTICE] Downloading 1 item(s)
		 * 
		 * 02/22 21:29:39 [NOTICE] Download complete:
		 * /Users/xuzewei/Downloads/test/lock_36.126611957796px_1149253_easyicon.net.png
		 * 
		 * Download Results: gid |stat|avg speed |path/URI
		 * ======+====+===========+=====================================================
		 * == 9add48|OK | 2.3KiB/s|/Users/xuzewei/Downloads/test/lock_36.
		 * 126611957796px_1149253_easyicon.net.png
		 * 
		 * Status Legend: (OK):download completed.
		 */
        String[] lines = commandOutput.split("\n");
        for (String line : lines) {
            if (line.contains("Download complete:")) {
                return line.replaceFirst("^.+:", "").trim();
            }
        }
        return null;
    }

    public static Response execute(Request request) throws Exception {
        Req req = (Req) request;
        MicroApiUtils.throwExceptionIfBlank(req.getUrl(), "url");
        Resp resp = new Resp();
        core(req, resp);
        return resp;
    }

    public static class Req extends Request {

        @Comment(value = "aria2c path")
        @Required(value = false)
        private String aria2cPath;

        public String getAria2cPath() {
            return aria2cPath;
        }

        public void setAria2cPath(String aria2cPath) {
            this.aria2cPath = aria2cPath;
        }

        @Comment(value = "文件url")
        @Required(value = true)
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Comment(value = "文件夹")
        @Required(value = false)
        private String dir;

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }
    }

    public static class Resp extends Response {

        @Comment(value = "文件名")
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
