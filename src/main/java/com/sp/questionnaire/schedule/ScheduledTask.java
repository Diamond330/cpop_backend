package com.sp.questionnaire.schedule;

import com.sp.questionnaire.entity.User;
import com.sp.questionnaire.service.UserService;
import com.sp.questionnaire.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTask {
    @Autowired
    private UserService userService;
    @Autowired
    private MailUtils mailUtils;

    @Scheduled(fixedRate = 30000000)
    public void scheduledTask() throws MessagingException {
//        System.out.println("任务执行时间：" + LocalDateTime.now());
        List<User> users = userService.queryUserByDate();
        for(User u:users){
            mailUtils.sendSurgeryDateMail(u.getEmail(), u.getSurgeryDate().toString());
            System.out.println(u.getUsername());

        }
    }

}
