package com.jr.service.Impl;

import com.github.pagehelper.PageInfo;
import com.jr.code.Code;
import com.jr.dao.IEmpUserDao;
import com.jr.exception.BusinessException;
import com.jr.pojo.EmpUser;
import com.jr.service.IEmpUserService;
import com.jr.util.MyBatisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

@Slf4j
public class EmpUserServiceImpl implements IEmpUserService {

    private static final SqlSession session = MyBatisUtil.openSession(true);
    private final IEmpUserDao empUserDaoImpl = session.getMapper(IEmpUserDao.class);




    @Override
    public EmpUser loginService(EmpUser empUser) {
        try {
            /*boolean flag = false;
            Integer empUserId = empUser.getEmpUserId();
            int count = empUserDaoImpl.countEmpUserById(empUserId);
            *//*count !=0 用户存在*//*
            if (count != 0) {
                EmpUser selectEmpUser = empUserDaoImpl.selectEmpUser(empUser);
                if (selectEmpUser != null) {
                    flag = true;
                }
            }
            return flag;*/

            EmpUser user = null;

            if (empUser != null) {
                Integer empUserId = empUser.getEmpUserId();
                int count = empUserDaoImpl.countEmpUserById(empUserId);
                /*count !=0 用户存在*/
                if (count != 0) {
                    user = empUserDaoImpl.selectEmpUser(empUser);
                }
            }
            return user;
        } catch (Exception e) {
            log.error("捕捉未知异常", e);
            throw new BusinessException(Code.BUSINESS_ERR, "参数错误");
        }
    }

    @Override
    public Integer registerService(EmpUser empUser) {
        try {
            if (empUser != null) {
                empUserDaoImpl.insertEmpUser(empUser);
                return empUserDaoImpl.selectMaxId();
            } else {
                throw new BusinessException(Code.BUSINESS_ERR, "参数错误");
            }
        } catch (Exception e) {
            log.error("捕捉未知异常", e);
            throw new BusinessException(Code.BUSINESS_ERR, "参数错误");
        }
    }
}
