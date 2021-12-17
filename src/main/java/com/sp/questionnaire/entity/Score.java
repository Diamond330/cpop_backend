package com.sp.questionnaire.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    private String id;
    private Integer score;
    private Date time;
}
