package com.sp.questionnaire.entity.view;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * description:返回所有Paper类的中间类，返回的是这个类的对象
 * Author:Shuhao Dong
 * Date:2021/11/8-15:17
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryView {
    private String id;//试卷ID
    private String username;
    private String email;
    private String name;
    private String age;
    private String gender;
    private String education;
    private String race;
    private String smokeHistory;
    private String etohHistory;
    private String comorbidity;//并发
}
