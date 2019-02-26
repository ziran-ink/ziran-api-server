package com.github.ziran_ink.ziran_api_server.utils;

import com.github.ziran_ink.itchat4j.beans.BaseMsg;
import com.github.ziran_ink.itchat4j.face.IMsgHandlerFace;

public class WeixinMsgHandler implements IMsgHandlerFace {

	@Override
	public String textMsgHandle(BaseMsg msg) {
//		MessageTools.sendMsgByNickName("逍遥津", "逍遥津");

//		try {
//			FileWriter w = new FileWriter("/Users/xuzewei/Downloads/getContactList.txt");
//			w.write(JSON.toJSONString(Core.getInstance().getContactList()));
//			w.close();
//		}catch (Exception e) {
//		}
		return null;
	}

	@Override
	public String picMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String voiceMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String viedoMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String nameCardMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sysMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public String verifyAddFriendMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String mediaMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

}
