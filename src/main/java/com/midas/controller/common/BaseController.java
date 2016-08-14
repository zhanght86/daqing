package com.midas.controller.common;



import org.springframework.beans.factory.annotation.Autowired;

import com.midas.constant.ResultCode;
import com.midas.constant.ResultMsg;
import com.midas.service.user.UserService;
import com.midas.vo.MessageBean;



public class BaseController {

	@Autowired
	private UserService userService;
	
    /**
     * 成功返回
     */
    protected MessageBean handleSuccess(){
        return new MessageBean(ResultCode.SUCCESS, ResultMsg.SECCESS_MSG );
    }

    /**
     * 失败返回
     */
    protected  MessageBean handleFail(String msg){
        return new MessageBean(ResultCode.ERROR,msg);
    }

    /**
     * 正常返回
     */
    protected MessageBean handleNormal(String msg){
        return new MessageBean(ResultCode.NORMAL,msg);
    }
}
