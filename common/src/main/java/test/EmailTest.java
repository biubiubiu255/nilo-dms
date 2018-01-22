package test;

import com.nilo.dms.common.MailInfo;
import com.nilo.dms.common.utils.SendEmailUtil;
import org.apache.commons.mail.EmailAttachment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/12/1.
 */
public class EmailTest {
    public static void main(String[] args) {

        String content = "测试图片：  <img hidefocus=\"true\" src=\"//www.baidu.com/img/bd_logo1.png\" width=\"270\" height=\"129\">";

        try {

            MailInfo mailInfo = new MailInfo();

            mailInfo.setContent(content);
            mailInfo.setSubject("放假通知-Test");
            mailInfo.setToAddress(Arrays.asList(new String[]{"ronny.zeng@kilimall.com", "rita.ding@kilimall.com"}));
            mailInfo.setCcAddress(Arrays.asList(new String[]{"ronny.zeng@kilimall.com", "rita.ding@kilimall.com"}));
            mailInfo.setBccAddress(Arrays.asList(new String[]{"ronny.zeng@kilimall.com", "rita.ding@kilimall.com"}));

            List<EmailAttachment> attachments = new ArrayList<>();
            EmailAttachment attachment = new EmailAttachment();
            attachment.setDescription("Hahahah");
            attachment.setPath("D:\\201458379665838080.xlsx");
            attachments.add(attachment);
            mailInfo.setAttachments(attachments);
            SendEmailUtil.sendEmail(mailInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


