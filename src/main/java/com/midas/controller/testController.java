package com.midas.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.constant.ErrorConstant;
import com.midas.constant.SysConstant;
import com.midas.exception.ServiceException;
import com.midas.service.UserService;
import com.midas.uitls.tools.ServletUtils;

@Controller
public class testController extends BaseDataController {

    @Autowired
    private UserService userSerivce;


    
    
    @RequestMapping(value = "/asyn/test")
    public @ResponseBody Callable<String> callable() {
    	  System.out.println("上海");
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("user_name", "sullivan");
                int i = userSerivce.getCountByCondition(map);
                return "Callable result";
            }
        };
    }
    
//    @RequestMapping(value = "/burn/mergeCheck", method = RequestMethod.GET)
//    @ResponseBody
//    public Map<String, Object> mergeCheck(String volLabel) {
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        logger.info("验证合并内容: {} 的唯一卷标内容", volLabel);
//        boolean bool = false;
//        String desc = "OK";
//        try {
//            bool = burnService.checkMerge(volLabel);
//        } catch (ServiceException e) {
//            desc = e.getMsg();
//        }
//        resultMap.put("result", bool);
//        resultMap.put("desc", desc);
//        return resultMap;
//    }

}
