package com.jr.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: ResponseMsg
 * <br></br>
 * className: ResponseMsg
 * <br></br>
 * packageName: com.myhd.util
 *
 * @author jinhui-huang
 * @version 1.0
 * @email 2634692718@qq.com
 * @Date: 2023/9/15 10:58
 */
public class ResponseMsg {
    public static final Map<String, HttpServletResponse> requestObj = new HashMap<>();

    public static void sendMsg(HttpServletResponse resp, Object obj) {
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter respWriter = resp.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(obj);
            respWriter.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
