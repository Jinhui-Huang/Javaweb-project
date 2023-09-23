package com.jr.controller;

import com.jr.code.Code;
import com.jr.pojo.EmpUser;
import com.jr.service.IEmpUserService;
import com.jr.service.Impl.EmpUserServiceImpl;
import com.jr.util.ReqRespMsgUtil;
import com.jr.util.Result;
import com.jr.util.TokenUtil;
import com.jr.util.VerifyCode;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@WebServlet("/login/*")
public class LoginServlet extends HttpServlet {
    private static final ArrayList<String> loginPaths = new ArrayList<>();

    static {
        loginPaths.add("login");

        loginPaths.add("code");
        loginPaths.add("test2");
    }

    private IEmpUserService empUserServiceImpl = new EmpUserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String[] split = requestURI.split("/");

        /* /login */
        if (split.length == 2) {
            String sign = TokenUtil.sign(new EmpUser(1001, "zhangsan", "jingli", null));
            Cookie cookie = new Cookie("token", sign);
            cookie.setMaxAge(60*60*24*2);
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            resp.addCookie(cookie);
            //ReqRespMsgUtil.sendMsg(resp, new Result(Code.GET_OK, sign, "使用GET请求测试"));
            /* 二级路径 */
        } else if(split.length == 3 && loginPaths.contains(split[2])) {
            switch (split[2]) {
                case "code":
                    makeCode(req, resp);
                    break;
                case "test2":
                    ReqRespMsgUtil.sendMsg(resp, new Result(Code.GET_OK, true, "这是测试路径"));
                    break;
                default:
                    break;
            }

        } else {
            ReqRespMsgUtil.sendMsg(resp, new Result(Code.BUSINESS_ERR, false, "请求路径不存在"));
        }
    }

    private void makeCode(HttpServletRequest req, HttpServletResponse resp) {
        ServletOutputStream outputStream = null;

        try {
            BufferedImage bufferedImage = new BufferedImage(100, 30, BufferedImage.TYPE_INT_RGB);
            String verifyCode = VerifyCode.getVerifyCode(bufferedImage);

            System.out.println("验证码==>" + verifyCode);
            resp.setContentType("image/jpeg");
            //int i = 1 / 0;
            outputStream = resp.getOutputStream();
            ImageIO.write(bufferedImage, "jpeg", outputStream);
            outputStream.close();
        } catch (Exception e) {
            try {
                assert outputStream != null;
                outputStream.close();
            } catch (Exception ex) {
                log.warn(ex.getMessage(), ex);
            }
            log.info(e.getMessage(), e);
            ReqRespMsgUtil.sendMsg(resp, new Result(Code.BUSINESS_ERR, false, "验证码异常"));

        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String[] split = requestURI.split("/");
        /* /login */
        if (split.length == 2) {
            /* 一级路径登陆功能 */
            EmpUser user = ReqRespMsgUtil.getMsg(req, resp, EmpUser.class);
            EmpUser signUser = empUserServiceImpl.loginService(user);
            if (signUser != null && user != null) {
                signUser.setEmpUserPwd(null);
                String sign = TokenUtil.sign(signUser);
                ArrayList<Cookie> cookieList = new ArrayList<>();
                cookieList.add(new Cookie("empUserId", signUser.getEmpUserId().toString()));
                cookieList.add(new Cookie("empUserName", URLEncoder.encode (signUser.getEmpUserName(), "UTF-8" )));
                //cookieList.add(new Cookie("empUserPwd", user.getEmpUserPwd()));
                cookieList.add(new Cookie("token", sign));

                for (Cookie cookie : cookieList) {
                    cookie.setMaxAge(60*60*24*2);
                    cookie.setPath("/");
                    cookie.setSecure(true);
                    cookie.setHttpOnly(true);
                    resp.addCookie(cookie);
                }
                Collection<String> headers = resp.getHeaders("Set-Cookie");
                resp.setHeader("Set-Cookie", "");
                for (String header : headers) {
                    resp.addHeader("Set-Cookie", header + " ; SameSite=None");
                }


                ReqRespMsgUtil.sendMsg(resp, new Result(Code.GET_OK, true, "登陆成功!"));
            } else {
                ReqRespMsgUtil.sendMsg(resp, new Result(Code.GET_ERR, false, "用户或密码错误"));
            }
        }
        else {
            ReqRespMsgUtil.sendMsg(resp, new Result(Code.BUSINESS_ERR, false, "路径不存在"));
        }
    }
}
