package com.midas.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.midas.uitls.tools.ServletUtils;

/**
 * 自定义拦截器
 * @author arron
 *
 */
public class MyInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(MyInterceptor.class);
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exception)
            throws Exception {
//        logger.info("afterCompletion 请求参数：" + ServletUtils.getParameters(request));
//        logger.info("afterCompletion RealPath : {}", ServletUtils.getRealPath(request));
//        logger.info("afterCompletion Object : {}", obj);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav)
            throws Exception {
        
//        logger.info("postHandle 请求参数：" + ServletUtils.getParameters(request));
//        logger.info("postHandle RealPath : {}", ServletUtils.getRealPath(request));
//        logger.info("postHandle Object : {}", obj);
//        logger.info("postHandle ModelAndView : {}", mav);
        
        
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
//        logger.info("preHandle 请求参数：" + ServletUtils.getParameters(request));
//        logger.info("preHandle RealPath : {}", ServletUtils.getRealPath(request));
        return true;
    }

}
