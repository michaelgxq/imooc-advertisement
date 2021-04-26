package com.imooc.advertisement;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @ClassName: TestMail
 * @Description: TODO
 * @author: yourName
 * @date: 2020年07月19日 19:23
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMail {
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    JavaMailSender mailSender;

    /**
     * 发送文本邮件
     * @param to 形参 to 接收收件方的地址
     * @param subject 形参 subject 接收邮件的主题
     * @param content 形参 content 接收邮件的内容
     */
    public void sendSimpleMail(String to, String subject, String content) {

        // 1.创建 Spring 提供的 SimpleMailMessage 类对象
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        // 2.调用 SimpleMailMessage 类对象中的 setTo() 方法设置收件方的邮箱地址
        simpleMailMessage.setTo(to);

        // 3.调用 SimpleMailMessage 类对象中的 setSubject() 方法设置邮件的主题
        simpleMailMessage.setSubject(subject);

        // 4.调用 SimpleMailMessage 类对象中的 setText() 方法设置邮件的正文
        simpleMailMessage.setText(content);

        // 5.调用 SimpleMailMessage 类对象中的 setFrom() 方法设置寄件人的地址
        simpleMailMessage.setFrom(from);

        // 6.调用 Spring 提供的 JavaMailSender 类中的 send() 方法来发送邮件，方法中传入上面创建的 SimpleMailMessage 类对象
        mailSender.send(simpleMailMessage);
    }


    /**
     * 发送 HTML 邮件
     * @param to 形参 to 接收收件方的地址
     * @param subject 形参 subject 接收邮件的主题
     * @param content 形参 content 接收邮件的内容
     */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {

        // 1.调用 Spring 提供的 JavaMailSender 类中的 createMimeMessage() 方法来创建 MimeMessage 类对象
        MimeMessage message = mailSender.createMimeMessage();

        // 2.创建 MimeMessageHelper 类对象，构造方法中第一个参数传入上面创建的 MimeMessage 类对象，第二个参数传入一个 true（含义暂时不知道）
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 3.调用 MimeMessageHelper 类对象中的 setFrom() 方法设置寄件人的地址
        helper.setFrom(from);

        // 4.调用 MimeMessageHelper 类对象中的 setTo() 方法设置收件方的邮箱地址
        helper.setTo(to);

        // 5.调用 MimeMessageHelper 类对象中的 setSubject() 方法设置邮件的主题
        helper.setSubject(subject);

        // 6.调用 MimeMessageHelper 类对象中的 setText() 方法设置邮件的正文，第一个参数传入邮件的正文，第二个参数传入 true，表示发送的是 HTML 格式的正文
        helper.setText(content, true);

        // 7.调用 Spring 提供的 JavaMailSender 类中的 send() 方法来发送邮件，方法中传入上面创建的 MimeMessageHelper 类对象
        mailSender.send(message);

    }


    /**
     * 发送带附件的邮件
     * @param to 形参 to 接收收件方的地址
     * @param subject 形参 subject 接收邮件的主题
     * @param content 形参 content 接收邮件的内容
     * @param filePath 形参 filePath 接收附件的文件路径
     */
    public void sendAttachmentMail(String to, String subject, String content,String filePath) throws MessagingException {

        // 1.调用 Spring 提供的 JavaMailSender 类中的 createMimeMessage() 方法来创建 MimeMessage 类对象
        MimeMessage message = mailSender.createMimeMessage();

        // 2.创建 MimeMessageHelper 类对象，构造方法中第一个参数传入上面创建的 MimeMessage 类对象，第二个参数传入一个 true（含义暂时不知道）
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 3.调用 MimeMessageHelper 类对象中的 setFrom() 方法设置寄件人的地址
        helper.setFrom(from);

        // 4.调用 MimeMessageHelper 类对象中的 setTo() 方法设置收件方的邮箱地址
        helper.setTo(to);

        // 5.调用 MimeMessageHelper 类对象中的 setSubject() 方法设置邮件的主题
        helper.setSubject(subject);

        // 6.调用 MimeMessageHelper 类对象中的 setText() 方法设置邮件的正文，第一个参数传入邮件的正文，第二个参数传入 true，表示发送的是 HTML 格式的正文
        helper.setText(content, true);

        // 7.创建 Spring 提供的 FileSystemResource 类对象，构造方法中传入指向形参 filePath 所接收的文件路径的 File 类对象
        FileSystemResource file = new FileSystemResource(new File(filePath));

        // 8.调用 FileSystemResource 类中的 getFilename() 方法来获取文件名
        String filename = file.getFilename();

        // 9.调用 MimeMessageHelper 类对象中的 addAttachment() 方法来为邮件添加附件，第一个参数传入上面获取的文件名，第二个参数传入上面创建的 FileSystemResource 类对象
        helper.addAttachment(filename, file);

        // 10.调用 Spring 提供的 JavaMailSender 类中的 send() 方法来发送邮件，方法中传入上面创建的 MimeMessageHelper 类对象
        mailSender.send(message);
    }


    /**
     * 发送带图片的邮件（它的本质就是在邮件中添加一个附带有我们传入的图片的路径的 img 标签，所以发送带图片的邮件时，我们要把邮件的正文设置成 HTML 文档）
     * @param to 形参 to 接收收件方的地址
     * @param subject 形参 subject 接收邮件的主题
     * @param content 形参 content 接收邮件的内容
     * @param rscPath 形参 recPath 接收图片的路径
     * @param rscId 形参 rscId 接收图片的 ID
     */
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) throws MessagingException {
        // 1.调用 Spring 提供的 JavaMailSender 类中的 createMimeMessage() 方法来创建 MimeMessage 类对象
        MimeMessage message = mailSender.createMimeMessage();

        // 2.创建 MimeMessageHelper 类对象，构造方法中第一个参数传入上面创建的 MimeMessage 类对象，第二个参数传入一个 true（含义暂时不知道）
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 3.调用 MimeMessageHelper 类对象中的 setFrom() 方法设置寄件人的地址
        helper.setFrom(from);

        // 4.调用 MimeMessageHelper 类对象中的 setTo() 方法设置收件方的邮箱地址
        helper.setTo(to);

        // 5.调用 MimeMessageHelper 类对象中的 setSubject() 方法设置邮件的主题
        helper.setSubject(subject);

        // 6.调用 MimeMessageHelper 类对象中的 setText() 方法设置邮件的正文，第一个参数传入邮件的正文，第二个参数传入 true，表示发送的是 HTML 格式的正文
        helper.setText(content, true);

        // 7.创建 Spring 提供的 FileSystemResource 类对象，构造方法中传入指向形参 rscPath 所接收的文件路径的 File 类对象
        FileSystemResource res = new FileSystemResource(new File(rscPath));

        // 8.调用 MimeMessageHelper 类对象中的 addInline() 方法来为邮件添加附件，第一个参数传入形参 recId 接收的图片的 ID，第二个参数传入上面创建的 FileSystemResource 类对象
        helper.addInline(rscId, res);

        // 9.调用 Spring 提供的 JavaMailSender 类中的 send() 方法来发送邮件，方法中传入上面创建的 MimeMessageHelper 类对象
        mailSender.send(message);
    }



    @Test
    @Ignore
    public void testMailSending() {

        // 发送文本邮件

//        sendSimpleMail("243184473@qq.com", "这是第三封邮件", "hello world");

        // 发送 HTML 文件

        // 创建邮件正文（即一个 HTML 文档）
//        String content = "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n" + "\t<meta charset=\"UTF-8\">\n" + "\t<title>html 模版</title>\n" + "</head>\n" + "<body>\n" + "\t<h2>hello world</h2>\n" + "</body>\n" + "</html>";
//
//        try {
//            sendHtmlMail("243184473@qq.com", "这是一封 HTML 邮件",content);
//        }
//        catch (MessagingException e) {
//            e.printStackTrace();
//        }


        // 发送带附件的邮件

//        String filePath = "C:\\Users\\24318\\Downloads\\XJad.rar";
//
//        try {
//            sendAttachmentMail("243184473@qq.com", "这是一封带附件的邮件", "content", filePath);
//        }
//        catch (MessagingException e) {
//            e.printStackTrace();
//        }


        // 发送带图片的邮件

        String imgPath = "C:\\Users\\24318\\Pictures\\timg.jpg";

        // 定义变量 recId 作为上面要添加的图片的 ID
        String recId = "new001";

        // 定义邮件的正文（即一个 HTML 文档，里面的 img 标签的 src 属性设置为上面定义的图片 ID）
        String content = "<html><body> 这是一个有图片的邮件：<img src=\'cid:" + recId + "\'></img></body></html>";

        try {
            sendInlineResourceMail("243184473@qq.com", "这是一封带图片的邮件", content, imgPath, recId);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
