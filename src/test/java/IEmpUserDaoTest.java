import com.jr.dao.IEmpUserDao;
import com.jr.pojo.EmpUser;
import com.jr.util.MyBatisUtil;
import com.jr.util.TokenUtil;
import com.mysql.cj.Session;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.BiConsumer;

/**
 * className IEmpDaoTest
 * packageName PACKAGE_NAME
 * Description
 *
 * @author "CYQH"
 * @version 1.0
 * @email 1660855825@qq.com
 * @Date: 2023/09/16 23:33
 */
public class IEmpUserDaoTest  {
    private static final SqlSession session = MyBatisUtil.openSession(true);

    private final IEmpUserDao empUserDaoImpl = session.getMapper(IEmpUserDao.class);


    @Test
    public void testCountEmpUser() {
        Integer num = empUserDaoImpl.countEmpUserById(1006565);
        System.out.println(num);
    }

    @Test
    public void insertEmpUser() {

        try {
            EmpUser empUser = new EmpUser();
            empUser.setEmpUserDuty("经理");
            empUser.setEmpUserName("张三");
            empUser.setEmpUserPwd("12345678");
            empUserDaoImpl.insertEmpUser(empUser);
            System.out.println(empUser.getEmpUserId());
        } catch (RuntimeException e) {
            session.rollback();
        } finally {
            session.close();
        }

    }

    @Test
    public void testJwtUtil() {
        EmpUser user = new EmpUser();
        user.setEmpUserName("张三");
        user.setEmpUserDuty("总经理");
        user.setEmpUserId(123456);
        String sign = TokenUtil.sign(user);
        System.out.println(sign);
        Map<String, Object> verify = TokenUtil.verify(sign, EmpUser.class);
        verify.forEach((s, o) -> System.out.println(s + "==> " + o.toString()));
    }

    @Test
    public void testSelectPage() {
        List<EmpUser> empUsers = empUserDaoImpl.selectByPage(1, 3);
        for (EmpUser empUser : empUsers) {
            System.out.println(empUser);
        }
    }

    @Test
    public void testSelectPage2() {
        List<EmpUser> empUsers = empUserDaoImpl.selectByPage2(new RowBounds(0, 3));
        for (EmpUser empUser : empUsers) {
            System.out.println(empUser);
        }
    }
}

