/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.winm.peiwang.utils;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 简单邮件（不带附件的邮件）发送器
 */
public class SimpleMailSender {
    /**
     * 以文本格式发送邮件
     * 
     * @param mailInfo
     *            待发送的邮件的信息
     */
    public boolean sendTextMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticator(mailInfo.getUserName(),
                    mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session
                .getInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
//            mailMessage.setText(mailContent);

            MimeMultipart multi = new MimeMultipart();   //代表整个邮件邮件
            BodyPart textBodyPart = new MimeBodyPart();  //设置正文对象
            textBodyPart.setText(mailContent);                  //设置正文
            multi.addBodyPart(textBodyPart);             //添加正文到邮件
            String[] files = mailInfo.getAttachFileNames();
            for (String path: files) {
                FileDataSource fds = new FileDataSource(path);   //获取磁盘文件
                BodyPart fileBodyPart = new MimeBodyPart();                       //创建BodyPart
                fileBodyPart.setDataHandler(new DataHandler(fds));           //将文件信息封装至BodyPart对象
                String fileNameNew = MimeUtility.encodeText(fds.getName(),
                        "utf-8", null);      //设置文件名称显示编码，解决乱码问题
                fileBodyPart.setFileName(fileNameNew);  //设置邮件中显示的附件文件名
                multi.addBodyPart(fileBodyPart);        //将附件添加到邮件中
            }
            mailMessage.setContent(multi);                      //将整个邮件添加到message中

            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 以HTML格式发送邮件
     * 
     * @param mailInfo
     *            待发送的邮件信息
     */
    public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(),
                    mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session
                .getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
