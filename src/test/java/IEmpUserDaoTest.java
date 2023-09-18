import com.jr.dao.IEmpUserDao;
import com.jr.pojo.EmpUser;
import com.jr.util.MyBatisUtil;
import com.mysql.cj.Session;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.sql.Date;
import java.util.List;

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

        EmpUser empUser = new EmpUser();
        empUser.setEmpUserDuty("经理");
        empUser.setEmpUserName("张三");
        empUser.setEmpUserPwd("12345678");
        Integer integer = empUserDaoImpl.insertEmpUser(empUser);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(empUserDaoImpl.selectMaxId());

    }
}
