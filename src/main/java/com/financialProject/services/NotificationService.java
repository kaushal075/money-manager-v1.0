package com.financialProject.services;

import com.financialProject.dto.ExpenseDto;
import com.financialProject.entity.ProfileEntity;
import com.financialProject.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    @Scheduled(cron = "0 0 22 * * *",zone = "IST")
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job started : sendDailyIncomeExpenseReminder() ");
        List<ProfileEntity> profiles = profileRepository.findAll();

        for(ProfileEntity profile : profiles){
            String body = "Hii ! "+profile.getFullName()+", <br/><br/>"
                    +"This is a friendly reminder for you to add your daily incomes and expenses on money manager.<br/><br/> "
                    +"<a href ="+frontendUrl+"style='display:inline-block';padding=10px 20ox;background-color: #4CAF50;:color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to money manager</a>"
                    +"<br/><br/>Best regards ,<br/> Money Manager Team..";

            emailService.sendEmail(profile.getEmail(),"daily reminder:Add your income and expense",body);
        }
        log.info("Job completed : sendDailyIncomeExpenseReminder() ");


    }
    @Scheduled(cron = "0 0 23 * * *",zone = "IST")
    public void sendDailyExpenseSummary(){
        log.info("Job started : sendDailyExpenseSummary()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile : profiles){
          List<ExpenseDto> todaysExpanse =  expenseService.getExpenseForUserOnDate(profile.getId(), LocalDate.now());
            if(!todaysExpanse.isEmpty()){
                StringBuilder table =  new StringBuilder();
                table.append("<table style= 'border-collapse: collapse;width: 100%;'>");
                table.append("<tr style='background-color: #f2f2f2;'><th style='border:1px solid #ddd;padding:8px'>S.No</th><th style ='border:1px solid #ddd;padding:8px'>Name</th><th style='border:1px solid #ddd;padding:8px'> Amount</th><th style='border:1px solid #ddd;padding:8px'> Category</th></tr>");
                int i = 1;
                for(ExpenseDto expenseDto : todaysExpanse){
                    table.append("<tr>");
                    table.append("<td style='border:1px solid #ddd;padding:8px'>").append(i++).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px'>").append(expenseDto.getName()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px'>").append(expenseDto.getAmount()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px'>")
                            .append(expenseDto.getCategoryId() !=null ? expenseDto.getCategoryId():"N/A")
                            .append("</td>");
                    table.append("<tr>");
                }
                table.append("</table>");
                String body = "Hello "+profile.getFullName()+" here is your is summary of your expanse for today : <br/><br/>"
                        +table+"<br/><br/>Best Regards ,<br/> Money Manager Team..";
                emailService.sendEmail(profile.getEmail(),"Your daily Expanse Summary :",body);


            }
            log.info("Job completed : sendDailyIncomeExpenseReminder()");

        }
    }
}
