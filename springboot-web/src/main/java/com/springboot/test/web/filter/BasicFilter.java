package com.springboot.test.web.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 添加拦截
 * options：跨域访问
 * Created by liufei on 2017/3/13 0013.
 */
@Component
public class BasicFilter implements Filter {

    public void destroy() {

    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterchain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 指定允许其他域名访问
        response.addHeader("Access-Control-Allow-Origin", "*");
        // 响应类型  响应方法
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        // 响应头设置
        response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type,token");
      //  response.addHeader("Access-Control-Allow-Headers", "POWERED-BY-FANTONG");

        response.addHeader("Access-Control-Max-Age", "30");




        // 需要过滤的代码
        filterchain.doFilter(servletRequest, servletResponse);
    }

    public void init(FilterConfig config) throws ServletException {

    }
}
