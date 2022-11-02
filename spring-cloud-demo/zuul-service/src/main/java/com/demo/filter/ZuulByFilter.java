package com.demo.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


@Component
public class ZuulByFilter extends ZuulFilter{

    //过滤器的类型 PRE_TYPE
    @Override
    public String filterType() {
        return "pre";
    }

    //过滤器的顺序
    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {

        //获取请求上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        //获取请求对象
        HttpServletRequest request = currentContext.getRequest();
        //获取请求的地址
        String requestURI = request.getRequestURI();
        //如果包含了login，则返回false
        return !requestURI.toUpperCase().contains("LOGIN");
    }

    //执行run方法-----中心方法
    @Override
    public Object run() throws ZuulException {

        //获取上下文对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        //获取对象
        HttpServletRequest request = currentContext.getRequest();
        //获取相应对象
        HttpServletResponse response = currentContext.getResponse();

        //获取请求头x-token
        String token = request.getHeader("x-token");
        //如果不存在请求头，则让重新登录
        if (StringUtils.isBlank(token)){
            currentContext.setSendZuulResponse(false);
            HashMap<String,String> map = new HashMap<>();
            map.put("success", "false");
            map.put("errorMessage","麻烦请登录" );
            //  返回错误信息
            currentContext.setResponseBody(JSON.toJSONString(map));
            //设置响应的数据格式
            response.setContentType("application/json;charset:utf-8");

        }else {
            response.setHeader("x-token", "ok");
        }
        return null;
    }
}
