package com.sp.questionnaire.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * description:
 * Author:Shuhao Dong
 * Date:2021/9/13-10:29
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    private String id;//用户ID

    //@Size(min = 2, max = 64, message = "昵称长度必须在2-64个字符")
    private String username;//用户名（昵称）

//    @NotNull(message = "密码不能为空")
//    @Size(min = 6, max = 64, message = "密码合理长度为必须6-64个字符")
    private String password;//MD5加密后的密码

//    @Email(message = "邮箱必须合法")
//    @NotNull(message = "邮箱不能为空")
    private String email;// 邮箱

    private Date createTime;//注册时间
    private Date surgeryDate;//最后登录时间
    private Integer status;//状态值：0：未激活1：已激活
    private String doctorId;
    private String randomCode;//随机码，用于激活用户
    private String name;
    private String age;
    private String gender;
    private String education;
    private String race;
    private String smokeHistory;
    private String etohHistory;
    private String comorbidity;//并发
}
