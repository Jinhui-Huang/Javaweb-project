package com.jr.config;

import com.jr.code.Code;
import com.jr.util.ReqRespMsgUtil;
import com.jr.util.Result;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: EmailFilter
 * <br></br>
 * className: EmailFilter
 * <br></br>
 * packageName: com.jr.config
 *
 * @author jinhui-huang
 * @version 1.0
 * @email 2634692718@qq.com
 * @Date: 2023/9/20 13:26
 */
@WebFilter("/email/*")
public class EmailFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (request.getRequestURI().equals("/email")) {
            filterChain.doFilter(request, response);
        } else {
            ReqRespMsgUtil.sendMsg(response, new Result(Code.BUSINESS_ERR, false, "路径访问错误"));
        }

    }
}
