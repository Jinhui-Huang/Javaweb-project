package com.jr.dao;

import com.jr.pojo.EmpUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IEmpUserDao {


    @Select("select count(*) from tb_empuser where empUserId = #{empUserId}")
    Integer countEmpUserById(Integer empUserId);


    @Select("select empUserId, empUserName, empUserDuty, empUserPwd from tb_empuser where empUserId = #{empUserId} and empUserPwd = #{empUserPwd}")
    EmpUser selectEmpUser(EmpUser empUser);


    @Insert("insert into tb_empuser(empUserName, empUserDuty, empUserPwd) values (#{empUserName}, #{empUserDuty}, #{empUserPwd})")
    Integer insertEmpUser(EmpUser empUser);

    @Select("select max(empUserId) from tb_empuser")
    Integer selectMaxId();


}
