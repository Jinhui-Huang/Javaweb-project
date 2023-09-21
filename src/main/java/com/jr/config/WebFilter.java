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
        /*Cookie[] cookies = request.getCookies();
        String url = "*";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("reqUrl")) {
                    url = cookie.getValue();
                }
            }
        }*/
        String url = "http://192.168.1.164:8081";
        response.setHeader("Access-Control-Allow-Origin", url);
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Set-Cookie");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
/*        response.setHeader("Set-Cookie", "HttpOnly;Secure;SameSite=None");*/

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
        /*cookie存储token, 缺点: 跨域无法传送cookie数据*/
        String cookie = request.getHeader("Cookie");
        String token = null;
        if (cookie != null && cookie.contains("token=")) {
            String[] split = cookie.split("token=");
            token = split[split.length - 1];
        }

        /*header存储*/
        /*String token = request.getHeader("Authorization");*/
        if (token != null) {
            Map<String, Object> verify = TokenUtil.verify(token, EmpUser.class);
            boolean flag = (boolean) verify.get("status");
            if (flag) {
                EmpUser user = (EmpUser) verify.get(EmpUser.class.getSimpleName());
                TokenUtil.SERVER_LOCAL.set(user);
                chain.doFilter(request, response);
                TokenUtil.SERVER_LOCAL.remove();
                return;
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