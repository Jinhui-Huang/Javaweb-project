import com.jr.pojo.EmpUser;
import com.jr.util.TokenUtil;
import org.junit.Test;

import java.util.Map;

/**
 * Description: TokenTest
 * <br></br>
 * className: TokenTest
 * <br></br>
 * packageName: PACKAGE_NAME
 *
 * @author jinhui-huang
 * @version 1.0
 * @email 2634692718@qq.com
 * @Date: 2023/9/20 11:24
 */
public class TokenTest {

    @Test
    public void testToken() {
        ThreadLocal<EmpUser> local = new ThreadLocal<>();
        EmpUser user = new EmpUser(1001, "zhangsan", "jingli", null);
        String token = TokenUtil.sign(user);
        System.out.println("生成token==>" + token);

        token += "ds";

        Map<String, Object> map = TokenUtil.verify(token, EmpUser.class);
        if ((Boolean) map.get("status")) {
            EmpUser verifyUser = (EmpUser) map.get(EmpUser.class.getSimpleName());
            System.out.println(verifyUser);
            local.set(verifyUser);
            verifyUser.setEmpUserPwd("1234");
            local.set(verifyUser);
            System.out.println(local.get());
        } else {
            System.out.println(map.get("msg"));
        }

    }
}
