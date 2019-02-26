package com.github.ziran_ink.ziran_api_server;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.github.microprograms.micro_api_runtime.annotation.MicroApi;
import com.github.microprograms.micro_api_runtime.enums.MicroApiReserveResponseCodeEnum;
import com.github.microprograms.micro_api_runtime.exception.MicroApiPassthroughException;
import com.github.microprograms.micro_api_runtime.model.Request;
import com.github.microprograms.micro_api_runtime.model.Response;
import com.github.microprograms.micro_nested_data_model_runtime.Comment;
import com.github.microprograms.micro_nested_data_model_runtime.Required;
import com.github.ziran_ink.web_weixin_sdk.core.Weixin;
import com.github.ziran_ink.web_weixin_sdk.core.impl.WeixinImpl;

import eu.maxschuster.dataurl.DataUrl;
import eu.maxschuster.dataurl.DataUrlBuilder;
import eu.maxschuster.dataurl.DataUrlEncoding;
import eu.maxschuster.dataurl.DataUrlSerializer;

@MicroApi(comment = "微信 - 获取登录二维码", version = "v0.0.5")
public class Weixin_LoadLoginQrCode_Api {
	public static final Weixin weixin = new WeixinImpl();

	public static void main(String[] args) throws Exception {
		Request req = new Request();
		System.out.println(JSON.toJSONString(execute(req)));

		Weixin_SendMsg_Api.Req smReq = new Weixin_SendMsg_Api.Req();
		smReq.setNickname("罗晓娟");
		smReq.setMsg("来自Scratch的测试消息😁");
		System.out.println(JSON.toJSONString(Weixin_SendMsg_Api.execute(smReq)));
	}

	private static void core(Request req, Resp resp) throws Exception {
		File qrcodeImgFile = File.createTempFile("weixin-login-qrcode", ".jpg");
		if (StringUtils.isBlank(weixin.getUuid())) {
			throw new MicroApiPassthroughException(MicroApiReserveResponseCodeEnum.unknown_exception);
		}
		if (!weixin.getQR(qrcodeImgFile.getPath())) {
			throw new MicroApiPassthroughException(MicroApiReserveResponseCodeEnum.unknown_exception);
		}
		try (FileInputStream is = new FileInputStream(qrcodeImgFile)) {
			byte[] buffer = new byte[(int) qrcodeImgFile.length()];
			IOUtils.readFully(is, buffer);
			DataUrl unserialized = new DataUrlBuilder().setMimeType("image/png").setEncoding(DataUrlEncoding.BASE64)
					.setData(buffer).build();
			String serialized = new DataUrlSerializer().serialize(unserialized);
			resp.setData(serialized);
		}
	}

	public static Response execute(Request request) throws Exception {
		Request req = request;
		Resp resp = new Resp();
		core(req, resp);
		return resp;
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
