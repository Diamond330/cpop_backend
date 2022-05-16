package com.sp.questionnaire.service.impl;

import com.sp.questionnaire.dao.ScoreDao;
import com.sp.questionnaire.dao.StudentDao;
import com.sp.questionnaire.entity.Score;
import com.sp.questionnaire.service.ScoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public int queryMean() {
        return scoreDao.queryMean();
    }
    @Override
    public int queryRange() {
        return scoreDao.queryRange();
    }

    @Override
    public int queryNumAll() {
        return scoreDao.queryNumAll();
    }

    @Override
    public List<Integer> queryStatic() {
        Map<String,List<String>> res = new HashMap<>();
        Map<String, Integer> map = scoreDao.queryStatic();
        TreeMap<String, Integer> map0 = new TreeMap<>();

        List<Integer> series = new LinkedList<>();

        for(String key: map.keySet()){
            map0.put(key,map.get(key));
        }
        for(String key: map0.keySet()){
            series.add(map.get(key));
        }
        return series;
    }
}

