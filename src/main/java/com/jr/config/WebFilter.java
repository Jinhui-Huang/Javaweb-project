package com.jr.config;

import com.jr.code.Code;
import com.jr.pojo.EmpUser;
import com.jr.util.ReqRespMsgUtil;
import com.jr.util.Result;
import com.jr.util.TokenUtil;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

@javax.servlet.annotation.WebFilter("*")
public class WebFilter implements Filter {
    /*过滤路径*/
    public static final ArrayList<String> paths = new ArrayList<>();
    static {
        paths.add("/");
        paths.add("login");
        paths.add("email");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("全路径过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        Cookie[] cookies = request.getCookies();
        String url = "*";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("reqUrl")) {
                    url = cookie.getValue();
                }
            }
        }
        response.setHeader("Access-Control-Allow-Origin", url);
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String requestURI = request.getRequestURI();
        if (paths.contains(requestURI)) {
            /* 单 / 进入*/
            chain.doFilter(request, response);

        } else if (countChar(requestURI, '/') < 20) {

            String[] split = requestURI.split("/");
            System.out.println("过滤路径==> " + split[1]);

            if (paths.contains(split[1])) {
                if (split[1].equals("login")) {
                    chain.doFilter(request, response);
                } else {
                    /*进行token校验*/
                    verifyToken(request, response, chain);
                }
            } else {
                ReqRespMsgUtil.sendMsg(response, new Result(Code.BUSINESS_ERR, false, "路径访问错误"));
            }
        } else {
            ReqRespMsgUtil.sendMsg(response, new Result(Code.BUSINESS_ERR, false, "路径不允许过多的 / "));
        }

    }

    private void verifyToken(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    if (token != null) {
                        Map<String, Object> verify = TokenUtil.verify(token, EmpUser.class);
                        Boolean status = (Boolean) verify.get("status");
                        if (status) {
                            EmpUser empUser = (EmpUser) verify.get(EmpUser.class.getSimpleName());
                            TokenUtil.SERVER_LOCAL.set(empUser);
                            chain.doFilter(request, response);
                            TokenUtil.SERVER_LOCAL.remove();
                            return;
                        }
                    }
                }
            }

        }
        ReqRespMsgUtil.sendMsg(response, new Result(Code.BUSINESS_ERR, false, "请登陆访问"));

    }

    private long countChar(String text, char targetChar) {
        return text.chars().filter(ch -> ch == targetChar).count();
    }

    @Override
    public void destroy() {
        System.out.println("全路径过滤器销毁");
    }

}