package com.sp.questionnaire.entity.view;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * description:添加Paper所定义的中间Question类
 * Author:Shuhao Dong
 * Date:2021/11/8-17:55
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AddPaperViewQuestion {
    private Integer questionType;
    private String questionTitle;
    private List<String> questionOption;

}
