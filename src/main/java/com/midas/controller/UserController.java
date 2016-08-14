//package com.midas.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.github.pagehelper.Page;
//import com.github.pagehelper.PageInfo;
//import com.midas.constant.ErrorConstant;
//import com.midas.constant.SysConstant;
//import com.midas.exception.ServiceException;
//import com.midas.service.UserService;
//import com.midas.uitls.tools.ServletUtils;
//
//@Controller
//public class UserController extends BaseDataController {
//
//    @Autowired
//    private UserService userSerivce;
//
//    @RequestMapping("/user/list")
//    public String list(HttpServletRequest request, HttpServletResponse response) {
//        Map<String, Object> map = ServletUtils.getParameters(request);
//        Page<?> page = new Page<Object>(getCurPage(map.get("pageNum")), SysConstant.PAGE_SIZE);
//        PageInfo<Map<String, Object>> pageInfo = userSerivce.list(map, page);
//        request.setAttribute("pageInfo", pageInfo);
//        return "user/list";
//    }
//
//    @RequestMapping(value = "/user/addInit")
//    public String addInit() {
//        return "user/add";
//    }
//    
//    @RequestMapping(value = "/user/updateInit")
//    public String updateInit(HttpServletRequest request, int id) {
//        Map<String, Object> map = userSerivce.getUserById(id);
//        request.setAttribute("user", map);
//        return "user/update";
//    }
//    
//
//    @RequestMapping(value = "/user/add")
//    public String add(HttpServletRequest request, String userName, String nickName, String password,
//            String password_confirm) {
//        try {
//            request.setAttribute("userName", userName);
//            request.setAttribute("nickName", nickName);
//            if (null == userName || "".equals(userName)) {
//                throw new ServiceException(ErrorConstant.CODE2000, "用户名不能为空");
//            }
//            if (null == nickName || "".equals(nickName)) {
//                throw new ServiceException(ErrorConstant.CODE2000, "真实名称不能为空");
//            }
//            if (null == password || "".equals(password)) {
//                throw new ServiceException(ErrorConstant.CODE2000, "密码不能为空");
//            }
//            if (null == password_confirm || "".equals(password_confirm)) {
//                throw new ServiceException(ErrorConstant.CODE2000, "确认密码不能为空");
//            }
//            if (!password.equals(password_confirm)) {
//                throw new ServiceException(ErrorConstant.CODE2000, "两次输入的密码不一样");
//            }
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("user_name", userName);
//            map.put("password", password);
//            map.put("nick_name", nickName);
//            userSerivce.addUser(map, 0);
//        } catch (ServiceException e) {
//            request.setAttribute("msg", e.getMsg());
//            return "user/add";
//        }
//        return "redirect:/user/list.do";
//    }
//
//    /**
//     * 删除用户
//     * 
//     * @return
//     */
//    @RequestMapping(value="/user/delete")
//    public String delete(String id) {
//         userSerivce.delete(id);
//         return "redirect:/user/list.do";
//    }
//
//    /**
//     * 重置密码
//     * 
//     * @return 调整页面
//     */
//    @RequestMapping(value = "/user/resetPassword")
//    @ResponseBody
//    public Map<String, Object> resetPassword(String username) {
//        boolean bool = false;
//        try {
//            bool = userSerivce.resetPassword(username);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("result", bool);
//        return map;
//    }
//
//    public String update() {
//        return null;
//    }
//
//    @RequestMapping(value = "/user/checkUserName")
//    @ResponseBody
//    public Map<String, Object> checkUserName(String username) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("user_name", username);
//        int i = userSerivce.getCountByCondition(map);
//        boolean bool = false;
//        if (i > 0) {
//            bool = true;
//        }
//        map.put("result", bool);
//        return map;
//    }
// 
//
//}
