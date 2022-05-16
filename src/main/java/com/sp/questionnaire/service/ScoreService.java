package com.sp.questionnaire.service;

import com.sp.questionnaire.entity.Score;
import com.sp.questionnaire.entity.Student;

import java.util.List;
import java.util.Map;

public interface ScoreService {
    boolean insertScore(Score score);
    List<Score> queryScore(String id);
    int queryMean();
    int queryRange();
    int queryNumAll();
    List<Integer> queryStatic();
}
