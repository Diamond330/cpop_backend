package com.sp.questionnaire.entity;

import com.sp.questionnaire.entity.view.QuestionAnswer;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

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
public class User<create_time> {

    private String id;//用户ID

//    @NotNull(message = "昵称不能为空")
//    @Size(min = 2, max = 64, message = "昵称长度必须在2-64个字符")
    private String username;//用户名（昵称）

    //@NotNull(message = "密码不能为空")
    @Size(min = 6, max = 64, message = "密码合理长度为必须6-64个字符")
    private String password;//MD5加密后的密码

//    @Email(message = "邮箱必须合法")
//    @NotNull(message = "邮箱不能为空")
    private String email;// 邮箱
    private String doctorEmail;// 邮箱
    private Date birth;
    private Date createTime;//注册时间
    private Date surgeryDate;//最后登录时间
    private Integer status;//状态值：0：未激活1：已激活
    private Integer identity;//身份：0：医生 2：病人
    private String randomCode;//随机码，用于激活用户
    private String parentId;//随机码，用于激活用户
    private String realName;
    private String age;
    private String gender;
    private String education;
    private String race;
    private String smokeHistory;
    private String etohHistory;
    private String comorbidity;//并发
    private String tobacco;
    private String alcohol;
    private String medicalMorbidity;
    private String steriodHistory;
    private String numBurst;
    private String diagnosis;
    private String drinkNum;
    private String smokeNum;
    private String antrostomy;
    private String antrostomyDir;
    private String ethPartial;
    private String ethPartialDir;
    private String total;
    private String totalDir;
    private String sphenoidotomy;
    private String sphenoidotomyDir;
    private String middleTurbinate;
    private String middleTurbinateDir;
    private String inferiorTurbinate;
    private String inferiorTurbinateDir;
    private String drafa;
    private String drafaDir;
    private String drafb;
    private String drafbDir;
    private String draf;
    private String septoplasty;
    private String septoplastyDir;
    private String revision;
    private String revisionDir;
    private List<Integer> scores;
    private List<Date> time;

}
