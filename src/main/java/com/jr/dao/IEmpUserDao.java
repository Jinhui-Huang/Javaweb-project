package com.jr.dao;

import com.jr.pojo.EmpUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface IEmpUserDao {


    @Select("select count(*) from tb_empuser where empUserId = #{empUserId}")
    Integer countEmpUserById(Integer empUserId);


    @Select("select empUserId, empUserName, empUserDuty, empUserPwd from tb_empuser where empUserId = #{empUserId} and empUserPwd = #{empUserPwd}")
    EmpUser selectEmpUser(EmpUser empUser);


    @Options(useGeneratedKeys = true, keyProperty = "empUserId", keyColumn = "empUserId")
    @Insert("insert into tb_empuser(empUserName, empUserDuty, empUserPwd) values (#{empUserName}, #{empUserDuty}, #{empUserPwd})")
    Integer insertEmpUser(EmpUser empUser);

    @Select("select max(empUserId) from tb_empuser")
    Integer selectMaxId();

    @Select("select * from tb_empuser limit #{startPage}, #{rows}")
    List<EmpUser> selectByPage(@Param("startPage") Integer startPage, @Param("rows") Integer rows);

    @Select("select * from tb_empuser")
    List<EmpUser> selectByPage2(RowBounds rowBounds);

    @Select("select * from tb_empuser")
    List<EmpUser> selectByPage3();


}
