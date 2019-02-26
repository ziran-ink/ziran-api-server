package com.github.ziran_ink.ziran_api_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.microprograms.micro_api_runtime.annotation.MicroApi;
import com.github.microprograms.micro_api_runtime.model.Request;
import com.github.microprograms.micro_api_runtime.model.Response;
import com.github.microprograms.micro_api_runtime.utils.MicroApiUtils;
import com.github.microprograms.micro_nested_data_model_runtime.Comment;
import com.github.microprograms.micro_nested_data_model_runtime.Required;
import com.github.ziran_ink.web_weixin_sdk.core.Weixin;
import com.github.ziran_ink.ziran_api_server.utils.WeixinMsgHandler;

@MicroApi(comment = "微信 - 发送消息", version = "v0.0.5")
public class Weixin_SendMsg_Api {
	private static Logger log = LoggerFactory.getLogger(Weixin_SendMsg_Api.class);

	private static void core(Req req, Resp resp) throws Exception {
		Weixin weixin = Weixin_LoadLoginQrCode_Api.weixin;

		if (!weixin.getStorage().isAlive()) {
			weixin.login();
			log.info("登陆成功");
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
		weixin.setUserInfo(); // 登陆成功后缓存本次登陆好友相关消息（NickName, UserName）

		log.info("开启微信状态检测线程");
		weixin.startCheckLoginStatus();

		log.info("开始消息处理线程");
		weixin.startHandleMsg(new WeixinMsgHandler());

		resp.setData(weixin.sendMsgByNickName(req.getMsg(), req.getNickname()) ? 1 : 0);
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
