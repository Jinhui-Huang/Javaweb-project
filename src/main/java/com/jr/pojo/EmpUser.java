package com.jr.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpUser {
    private Integer empUserId;
    private String empUserName;
    private String empUserDuty;
    private String empUserPwd;
}
