package com.sp.questionnaire.service.impl;

import com.sp.questionnaire.dao.PaperDao;
import com.sp.questionnaire.entity.Answer;
import com.sp.questionnaire.entity.Paper;
import com.sp.questionnaire.entity.Question;
import com.sp.questionnaire.entity.view.*;
import com.sp.questionnaire.service.AnswerService;
import com.sp.questionnaire.service.PaperService;
import com.sp.questionnaire.service.QuestionService;
import com.sp.questionnaire.utils.CommonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * description:
 * Author:Shuhao Dong
 * Date:2021/9/13-14:59
 */
@Service
public class PaperServiceImpl implements PaperService {
    //private final static int dataPaperViewQuestionNum = 100;
    @Autowired
    private PaperDao paperDao;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private PaperMethodHelp paperMethodHelp;

    @Override
    public List<Paper> queryPaper() {
        return paperDao.queryPaper();
    }

    @Override
    public List<Paper> queryPaperByUserID(String userId) {
        return paperDao.queryPaperByUserID(userId);
    }

    @Override
    public Paper queryPaperByID(String id) {
        return paperDao.queryPaperByID(id);
    }

    @Transactional
    @Override
    public boolean insertPaper(Paper paper) {
        if (paper != null && !"".equals(paper.getId())) {
            try {
                //System.err.println(paper.toString());
                int i = paperDao.insertPaper(paper);
                if (i == 1) {
                    return true;
                } else {
                    throw new RuntimeException("a:?????????????????????" + paper);
                }
            } catch (Exception e) {
                throw new RuntimeException("b:?????????????????????" + e.getMessage());
            }
        } else {
            throw new RuntimeException("c:?????????????????????Paper???id???????????????");
        }
    }

    @Transactional
    @Override
    public boolean updatePaper(Paper paper) {
        if (paper != null && !"".equals(paper.getId())) {
            try {
                int i = paperDao.updatePaper(paper);
                if (i == 1) {
                    return true;
                } else {
                    throw new RuntimeException("a:?????????????????????" + paper);
                }
            } catch (Exception e) {
                throw new RuntimeException("b:?????????????????????" + e.getMessage());
            }
        } else {
            throw new RuntimeException("c:?????????????????????Paper???id???????????????");
        }
    }

    @Transactional
    @Override
    public boolean deletePaper(String id) {
        if (id != null && !"".equals(id)) {
            try {
                //System.out.println(id);
                int i = paperDao.deletePaper(id);
                if (i == 1) {
                    return true;
                } else {
                    throw new RuntimeException("a:?????????????????????" + id);
                }
            } catch (Exception e) {
                throw new RuntimeException("b:?????????????????????" + e.getMessage());
            }
        } else {
            throw new RuntimeException("c:?????????????????????Paper???id???????????????");
        }
    }

    @Override
    public List<Paper> queryPaperByTitle(String title) {
        return paperDao.queryPaperByTitle(title);
    }

    @Transactional
    @Override
    public boolean deleteManyPaper(List<String> id) {
        if (id == null || id.size() <= 0) {
            throw new RuntimeException("c:?????????????????????Paper???id???????????????");
        }
        //int t = id.size();
        for (String i : id) {
            Paper paper = queryPaperByID(i);
            paper.setStatus(3);
            updatePaper(paper);
        }


        return true;
    }

    //??????Paper???data
    @Transactional
    @Override
    public Object dataPaper(String id) throws ParseException {
        Paper paper = queryPaperByID(id);
        //System.out.println(id);
        //??????id??????paper??????
        if (paper != null) {


            //System.out.println("paper" + paper);

            //?????????????????????dataPaperViewPaper??????????????????questions???content
            DataPaperViewPaper dataPaperViewPaper = new DataPaperViewPaper();

            //??????DataPaperViewPaper??????questions??????????????????????????????
            List<DataPaperViewQuestion> questions = new ArrayList<>();
            //???????????????List???????????????????????????????????????
            DataPaperViewQuestion dataPaperViewQuestion = new DataPaperViewQuestion();
            //for (int i = 0; i < dataPaperViewQuestionNum; i++) {//???????????????
            //    dataPaperViewQuestion[i] = new DataPaperViewQuestion();
            //}
            int num = 0;//??????????????????

            //System.out.println("paperid"+paper.getId());
            //??????paperId?????????????????????list1????????????1??????????????????list2??????????????????2???list3???3
            List<Question> list1 = questionService.getQuestionsByPaperIdAndQuestionType(paper.getId(), 1);
            List<Question> list2 = questionService.getQuestionsByPaperIdAndQuestionType(paper.getId(), 2);
            List<Question> list3 = questionService.getQuestionsByPaperIdAndQuestionType(paper.getId(), 3);


            JSONObject json = new JSONObject();
            json.put("id", paper.getId());
            json.put("title", paper.getTitle());
            json.put("status", paper.getStatus());
            json.put("createTime", commonUtils.getLongByDate(paper.getCreateTime()));
            json.put("startTime", commonUtils.getDateStringByDate(paper.getStartTime()));
            json.put("endTime", commonUtils.getDateStringByDate(paper.getEndTime()));
            json.put("totalCount", (list1.size() == 0) ? 0 : answerService.countAnswer(id, list1.get(0).getId()));

            JSONArray jsonArray = new JSONArray();


            //????????????????????????????????????????????????
            dataPaperViewPaper.setId(paper.getId())
                    .setTitle(paper.getTitle())
                    .setStatus(paper.getStatus())
                    .setCreateTime(commonUtils.getLongByDate(paper.getCreateTime()))
                    .setStartTime(commonUtils.getDateStringByDate(paper.getStartTime()))
                    .setEndTime(commonUtils.getDateStringByDate(paper.getEndTime()))
                    .setTotalCount((list1.size() == 0) ? 0 : answerService.countAnswer(id, list1.get(0).getId()));//?????????????????????

            if (list1.size() > 0) {//???????????????1--?????????
                for (Question question : list1) {//?????????????????????

                    //System.out.println("questionOption============"+question.getQuestionOption().substring(1,question.getQuestionOption().length()-1));
                    //??????????????????????????????,????????????
                    //System.out.println("___----" + JSONArray.fromObject(question.getQuestionOption()));
                    //[java,c,qq,aa]
                    //["java","c","qq","aa"]
                    Object[] o = JSONArray.fromObject(question.getQuestionOption()).toArray();
                    String[] options = new String[o.length];
                    for (int i = 0, n = o.length; i < n; i++) {
                        options[i] = o[i].toString();
                    }
                    //??????????????????List??????????????????QuestionOption?????????????????????????????????????????????questions???????????????QuestionOption
                    List<String> a = new ArrayList<>();
                    Collections.addAll(a, options);//??????????????????????????????

                    //??????????????????????????????AnswerContent???????????????b???
                    List<Object> b = new ArrayList<>();
                    List<Answer> listAnswer = answerService.queryAnswerByQuestionId(question.getId());
                    //String option = question.getQuestionOption();//???????????????????????????QuestionOption
                    //[java,c,qq,aa]
                    //System.out.println(option);
                    //String[] ops = {"a", "b", "c"};
                    //String[] ops = option.substring(1, option.length() - 1).split(",");

                    //???????????????????????????????????????????????????????????????
                    int[] ansContent = new int[options.length];

                    /*for (int i=0,n=options.length;i<n;i++){
                        System.out.println("??????options???"+options[i]);
                    }*/

                    for (Answer an : listAnswer) {//????????????,??????answerContent
                        //System.out.println(an.getAnswerContent());
                        for (int i = 0, n = options.length; i < n; i++) {
                            //System.out.println("??????answerContent"+an.getAnswerContent());
                            if (an.getAnswerContent().length() > 4) {
                                if (an.getAnswerContent().substring(2, an.getAnswerContent().length() - 2).equals(options[i])) {
                                    //???????????????????????????????????????
                                    ansContent[i]++;
                                }
                            }
                        }
                    }

                    //????????????????????????????????????????????????Questions??????answerContent  ???b???
                    Collections.addAll(b, ansContent);

                    JSONArray ja = new JSONArray();//??????
                    ja.addAll(JSONArray.fromObject(options));
                    JSONArray jTimes = new JSONArray();//????????????
                    jTimes.addAll(JSONArray.fromObject(ansContent));

                    JSONObject jo = new JSONObject();
                    jo.put("id", question.getId());
                    jo.put("questionType", 1);
                    jo.put("questionTitle", question.getQuestionTitle());
                    jo.put("questionOption", ja);//??????
                    jo.put("answerContent", jTimes);//??????????????????

                    jsonArray.add(jo);


                    dataPaperViewQuestion = new DataPaperViewQuestion();
                    //?????????question???????????????question???,questions??????????????????????????????dataPaperViewQuestion
                    dataPaperViewQuestion.setId(question.getId())
                            .setQuestionType(1)
                            .setQuestionTitle(question.getQuestionTitle())
                            .setQuestionOption(a)
                            .setAnswerContent(b);


                    //JSONObject json = new JSONObject();
                    //json.put("before", questions);
                    //System.out.println("json1:"+json);

                    //??????????????????question????????????questions?????????,question??????????????????????????????1
                    questions.add(dataPaperViewQuestion);
                    //num++;
                    //json = new JSONObject();
                    //json.put("data1", questions);
                    //System.out.println("json1:"+json);

                    //???questions????????????????????????????????????
                    dataPaperViewPaper.setQuestions(questions);

                }
                /*JSONObject json = new JSONObject();
                json.put("data1", dataPaperViewPaper);
                System.out.println("json1:"+json);*/
                //return dataPaperViewPaper;
            }


            if (list2.size() > 0) {//???????????????2--?????????

                for (Question q : list2) {
                    //??????????????????????????????,????????????

                    //String[] options = q.getQuestionOption().substring(1, q.getQuestionOption().length() - 1).split(",");
                    //String[] options = (String[]) JSONArray.fromObject(q.getQuestionOption()).toArray();
                    Object[] o = JSONArray.fromObject(q.getQuestionOption()).toArray();
                    String[] options = new String[o.length];
                    for (int i = 0, n = o.length; i < n; i++) {
                        options[i] = o[i].toString();
                    }
                    //??????????????????List??????????????????QuestionOption?????????????????????????????????????????????questions???????????????QuestionOption
                    List<String> a = new ArrayList<>();
                    Collections.addAll(a, options);

                    //??????????????????????????????AnswerContent???????????????b???
                    List<Object> b = new ArrayList<>();

                    //?????????????????????
                    List<Answer> listAnswer = answerService.queryAnswerByQuestionId(q.getId());
                    //String[] option = {"a","b","c"};
                    //String option = q.getQuestionOption();//???????????????????????????QuestionOption
                    //[java,c,qq,aa]
                    //System.out.println(option);
                    //String[] ops = {"a", "b", "c"};//option.substring(1, option.length() - 1).split(",");

                    //???????????????????????????????????????????????????????????????
                    int[] ansContent = new int[options.length];//??????????????????

                    /* for (int i=0,n=options.length;i<n;i++){
                        System.out.println("??????options???"+options[i]);
                    }*/

                    for (Answer an : listAnswer) {//????????????,??????answerContent

                        //System.out.println(an.getAnswerContent());
                        //[java,qq,aa]
                        String temp="";
                        if (an.getAnswerContent().length() > 2) {
                            temp = an.getAnswerContent().substring(1, an.getAnswerContent().length() - 1);//?????????["java","qq","aa"]
                        }
                        String[] answerContents=null;
                        if (temp!=null){
                        //??????an.getAnswerContent()????????????????????????????????????
                        answerContents = temp.split(",");
                        }
                        //System.out.println("??????AnswerContent???"+an.getAnswerContent());//[java,qq,aa]
                        for (int i = 0, n = options.length; i < n; i++) {
                            for (int j = 0, m = answerContents.length; j < m; j++) {
                                if (answerContents.length > 2) {
                                    if (answerContents[j].substring(1, answerContents[j].length() - 1).equals(options[i])) {
                                        ansContent[i]++;
                                    }
                                }
                            }

                        }
                    }
                    Collections.addAll(b, ansContent);

                    JSONArray ja = new JSONArray();//??????
                    ja.addAll(JSONArray.fromObject(options));
                    JSONArray jTimes = new JSONArray();//????????????
                    jTimes.addAll(JSONArray.fromObject(ansContent));

                    JSONObject jo = new JSONObject();
                    jo.put("id", q.getId());
                    jo.put("questionType", 2);
                    jo.put("questionTitle", q.getQuestionTitle());
                    jo.put("questionOption", ja);//??????
                    jo.put("answerContent", jTimes);//??????????????????

                    jsonArray.add(jo);

                    dataPaperViewQuestion = new DataPaperViewQuestion();

                    //?????????????????????
                    //System.out.println("questions0============="+questions);
                    dataPaperViewQuestion.setId(q.getId())
                            .setQuestionType(2)
                            .setQuestionTitle(q.getQuestionTitle())
                            .setQuestionOption(a)
                            .setAnswerContent(b);
                    //System.out.println("questions1============="+questions);
                    questions.add(dataPaperViewQuestion);
                    //num++;
                    //System.out.println("questions2============="+questions);
                    /*JSONObject json = new JSONObject();
                    json.put("before", questions);
                    System.out.println("json1-2:"+json);*/

                    dataPaperViewPaper.setQuestions(questions);

                    /*json = new JSONObject();
                    json.put("after", questions);
                    System.out.println("json1-2:"+json);*/

                }
                //JSONObject json = new JSONObject();
                //json.put("data2", dataPaperViewPaper);
                //System.out.println("json2:" + json);
                //return dataPaperViewPaper;
            }

            if (list3.size() > 0) {//????????????????????????

                for (Question questionThree : list3) {

                    /*"id": "3234", "questionType":3,
                            "questionTitle": "?????????????????????????????????",
                            "questionOption": [],
                    "answerContent": [
                            "?????????",
                            "?????????",
                            "?????????"
                        ]*/

                    //?????????????????????,????????????????????????????????????????????????????????????????????????answerContent  ??????????????????b
                    List<Answer> listAnswer = answerService.queryAnswerByQuestionId(questionThree.getId());
                    List<Object> b = new ArrayList<>();
                    for (Answer a : listAnswer) {
                        if (a.getAnswerContent().length() > 4) {
                            b.add(a.getAnswerContent().substring(2, a.getAnswerContent().length() - 2));
                        }
                    }


                    JSONArray ja = new JSONArray();//??????,????????????
                    //ja.addAll(JSONArray.fromObject(options));
                    JSONArray jTimes = new JSONArray();//????????????
                    jTimes = JSONArray.fromObject(b);

                    JSONObject jo = new JSONObject();
                    jo.put("id", questionThree.getId());
                    jo.put("questionType", 3);
                    jo.put("questionTitle", questionThree.getQuestionTitle());
                    jo.put("questionOption", ja);//??????
                    jo.put("answerContent", jTimes);//??????????????????

                    jsonArray.add(jo);

                    dataPaperViewQuestion = new DataPaperViewQuestion();

                    //??????????????????
                    // System.out.println("num==========="+num);
                    dataPaperViewQuestion.setId(questionThree.getId())
                            .setQuestionType(3)
                            .setQuestionTitle(questionThree.getQuestionTitle())
                            .setQuestionOption(new ArrayList<String>())
                            .setAnswerContent(b);


                    //JSONObject json = new JSONObject();
                    //json.put("before", questions);
                    //System.out.println("json1:"+json);

                    questions.add(dataPaperViewQuestion);
                    //num++;
                    //json = new JSONObject();
                    //json.put("data1", questions);
                    //System.out.println("json1:"+json);

                    dataPaperViewPaper.setQuestions(questions);

                }
                //JSONObject json = new JSONObject();
                //json.put("data3", dataPaperViewPaper);
                //System.out.println("json3:" + json);

            }


            if (json != null) {
                json.put("questions", jsonArray);
                return json;//??????????????????
            } else return null;


        }
        return null;
    }


    @Override
    @Transactional
    public boolean updatePaperQuestions(UpdatePaperViewPaper paper, String userId, AddPaperViewPaper addPaperViewPaper) throws ParseException {

        deletePaper(paper.getId());
        questionService.deleteQuestionsByPaperId(paper.getId());
        //PaperMethodHelp paperMethodHelp = new PaperMethodHelp();
        paperMethodHelp.insertPaper(addPaperViewPaper, userId, paper.getId());

        return true;
    }


}

