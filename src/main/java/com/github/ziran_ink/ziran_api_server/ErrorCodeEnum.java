package com.github.ziran_ink.ziran_api_server;

import com.github.microprograms.micro_api_runtime.model.ResponseCode;

public enum ErrorCodeEnum implements ResponseCode {

    /**Token已失效，请重新登录*/
    invalid_token(1010, "Token已失效，请重新登录"), /**密码错误*/
    invalid_password(1011, "密码错误"), /**微信登录失败*/
    weixin_login_error(1012, "微信登录失败"), /**微信还没有登录*/
    weixin_need_login(1013, "微信还没有登录"), /**微信发送消息失败*/
    weixin_send_msg_error(1014, "微信发送消息失败");

    private ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;

    public int getCode() {
        return code;
    }

    private final String message;

    public String getMessage() {
        return message;
    }
}
