package com.jr.service;

import com.jr.pojo.EmpUser;

public interface IEmpUserService {


    Boolean loginService(EmpUser empUser);

    Integer registerService(EmpUser empUser);

}
