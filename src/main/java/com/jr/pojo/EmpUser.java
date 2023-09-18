package com.jr.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpUser {
    private Integer empUserId;
    private String empUserName;
    private String empUserDuty;
    private String empUserPwd;
}
