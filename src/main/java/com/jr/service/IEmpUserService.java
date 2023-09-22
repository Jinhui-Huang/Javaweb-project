package com.jr.service;

import com.github.pagehelper.PageInfo;
import com.jr.pojo.EmpUser;

import java.util.List;

public interface IEmpUserService {


    EmpUser loginService(EmpUser empUser);

    Integer registerService(EmpUser empUser);

}
