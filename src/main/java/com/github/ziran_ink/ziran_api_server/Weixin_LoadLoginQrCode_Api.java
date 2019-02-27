package com.github.ziran_ink.ziran_api_server;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.github.ziran_ink.web_weixin_sdk.core.impl.WeixinImpl;
import com.github.ziran_ink.ziran_api_server.utils.Tokens;
import com.github.ziran_ink.ziran_api_server.utils.WeixinMsgHandler;
import eu.maxschuster.dataurl.DataUrl;
import eu.maxschuster.dataurl.DataUrlBuilder;
import eu.maxschuster.dataurl.DataUrlEncoding;
import eu.maxschuster.dataurl.DataUrlSerializer;

@MicroApi(comment = "微信 - 获取登录二维码", version = "v0.0.5")
public class Weixin_LoadLoginQrCode_Api {

	private static Logger log = LoggerFactory.getLogger(Weixin_LoadLoginQrCode_Api.class);

	private static void core(Req req, Resp resp) throws Exception {
		if (!Tokens.isValidToken(req.getToken())) {
			throw new MicroApiPassthroughException(ErrorCodeEnum.invalid_token);
		}
		Tokens.setTokenProp(req.getToken(), "weixin", new WeixinImpl());
		Weixin weixin = Tokens.getTokenProp(req.getToken(), "weixin");
		File qrcodeImgFile = File.createTempFile("weixin-login-qrcode", ".jpg");
		if (StringUtils.isBlank(weixin.getUuid())) {
			throw new MicroApiPassthroughException(ErrorCodeEnum.weixin_login_error);
		}
		if (!weixin.getQR(qrcodeImgFile.getPath())) {
			throw new MicroApiPassthroughException(ErrorCodeEnum.weixin_login_error);
		}
		try (FileInputStream is = new FileInputStream(qrcodeImgFile)) {
			byte[] buffer = new byte[(int) qrcodeImgFile.length()];
			IOUtils.readFully(is, buffer);
			DataUrl unserialized = new DataUrlBuilder().setMimeType("image/png").setEncoding(DataUrlEncoding.BASE64)
					.setData(buffer).build();
			String serialized = new DataUrlSerializer().serialize(unserialized);
			resp.setData(serialized);
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (!weixin.getStorage().isAlive()) {
					weixin.login();
				}
				log.info("微信初始化");
				if (!weixin.webWxInit()) {
					log.error("微信初始化异常");
				}
				log.info("开启微信状态通知");
				weixin.wxStatusNotify();
				log.info(String.format("欢迎回来， %s", weixin.getStorage().getNickName()));
				log.info("开始接收消息");
				weixin.startReceiving();
				log.info("获取联系人信息");
				weixin.webWxGetContact();
				log.info("获取群好友及群好友列表");
				weixin.WebWxBatchGetContact();
				log.info("缓存本次登陆好友相关消息");
				// 登陆成功后缓存本次登陆好友相关消息（NickName, UserName）
				weixin.setUserInfo();
				log.info("开启微信状态检测线程");
				weixin.startCheckLoginStatus();
				log.info("开始消息处理线程");
				weixin.startHandleMsg(new WeixinMsgHandler());
			}
		}).start();
	}

	public static Response execute(Request request) throws Exception {
		Req req = (Req) request;
		MicroApiUtils.throwExceptionIfBlank(req.getToken(), "token");
		Resp resp = new Resp();
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
	}

	public static class Resp extends Response {

		@Comment(value = "登录二维码")
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
