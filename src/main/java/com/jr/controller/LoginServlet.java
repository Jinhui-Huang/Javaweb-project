package com.jr.controller;

import com.jr.code.Code;
import com.jr.pojo.EmpUser;
import com.jr.service.IEmpUserService;
import com.jr.service.Impl.EmpUserServiceImpl;
import com.jr.util.ReqRespMsgUtil;
import com.jr.util.Result;
import com.jr.util.TokenUtil;
import com.jr.util.VerifyCode;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/login/*")
public class LoginServlet extends HttpServlet {
    private static final ArrayList<String> loginPaths = new ArrayList<>();

    private final Logger logger = Logger.getLogger(LoginServlet.class);

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
            ReqRespMsgUtil.sendMsg(resp, new Result(Code.BUSINESS_ERR, false, "登录不允许使用GET请求"));
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

            outputStream = resp.getOutputStream();
            ImageIO.write(bufferedImage, "jpeg", outputStream);
            outputStream.close();
        } catch (IOException e) {
            try {
                assert outputStream != null;
                outputStream.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
            logger.error(e);
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
            if (signUser != null) {
                signUser.setEmpUserPwd(null);
                String sign = TokenUtil.sign(signUser);
                resp.setHeader("token", sign);
                ReqRespMsgUtil.sendMsg(resp, new Result(Code.GET_ERR, signUser, "登陆成功!"));
            } else {
                ReqRespMsgUtil.sendMsg(resp, new Result(Code.GET_ERR, false, "用户或密码错误"));
            }
        }
        else {
            ReqRespMsgUtil.sendMsg(resp, new Result(Code.BUSINESS_ERR, false, "路径不存在"));
        }
    }
}
