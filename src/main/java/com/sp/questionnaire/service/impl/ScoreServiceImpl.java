package com.sp.questionnaire.service.impl;

import com.sp.questionnaire.dao.ScoreDao;
import com.sp.questionnaire.dao.StudentDao;
import com.sp.questionnaire.entity.Score;
import com.sp.questionnaire.service.ScoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private ScoreDao scoreDao;
    @Override
    public boolean insertScore(Score score) {
        if (score != null && !"".equals(score.getId())) {
            score.setTime(new Date());
            try {
                int i = scoreDao.insertScore(score);
                if (i == 1) {
                    return true;
                } else {
                    throw new RuntimeException("插入失败！" + score);
                }
            } catch (Exception e) {
                throw new RuntimeException("插入失败：" + e.getMessage());
            }
        } else {
            throw new RuntimeException("id不能为空！");
        }
    }

    @Override
    public List<Score> queryScore(String id) {
        return scoreDao.queryScore(id);
    }
}

