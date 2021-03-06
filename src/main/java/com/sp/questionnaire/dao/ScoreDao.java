package com.sp.questionnaire.dao;

import com.sp.questionnaire.entity.Score;
import com.sp.questionnaire.entity.Student;

import java.util.HashMap;
import java.util.List;

public interface ScoreDao {
    int insertScore(Score score);
    List<Score> queryScore(String id);
    int queryMean();
    int queryRange();

    int queryNumAll();
    HashMap<String,Integer> queryStatic();
}
