package com.jr.controller;

import com.jr.code.Code;
import com.jr.util.ReqRespMsgUtil;
import com.jr.util.Result;
import com.jr.util.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: EmailServlet
 * <br></br>
 * className: EmailServlet
 * <br></br>
 * packageName: com.jr.controller
 *
 * @author jinhui-huang
 * @version 1.0
 * @email 2634692718@qq.com
 * @Date: 2023/9/20 13:23
 */
@WebServlet("/email")
public class EmailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ReqRespMsgUtil.sendMsg(resp, new Result(Code.GET_OK, TokenUtil.SERVER_LOCAL.get(), "发送邮件成功"));
    }
}
