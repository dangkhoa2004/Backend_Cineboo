/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend.cineboo.utility;

import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class EmailHelper {

    private   String username="huyvhpp02961@fpt.edu.vn";
    private   String password="jcom abxl bbsp gcsh";
    private final Properties p;

    public EmailHelper(String username, String password) {
        this.username = username;
        this.password = password;
        p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", 587);
    }
    public EmailHelper() {
        p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", 587);
        p.put("mail.smtp.ssl.protocols", "TLSv1.2");
    }
    public void sendEmail(String from, String to, String subject, String messageContent) throws AddressException, MessagingException {

            //Create a new session
            Session session = Session.getInstance(p, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            //Create a fodder, setting up basic information
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            //Now the body
            Multipart body = new MimeMultipart();
            MimeBodyPart body1 = new MimeBodyPart();
            body1.setText(messageContent);
            body.addBodyPart(body1);

            //shove the content into the fodder
            message.setContent(body);
            //and SEND!
            Transport.send(message);

    }

    public void sendEmailWithEmbeddedImage(String from, String to, String subject, String messageContent, BufferedImage image, String cid) throws IOException {
        try {
            //Create a new session
            Session session = Session.getInstance(p, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            //Create a fodder, setting up basic information
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            //Now the body
            Multipart body = new MimeMultipart();
            MimeBodyPart body1 = new MimeBodyPart();


            body1.setText(messageContent, "US-ASCII", "html");
            body.addBodyPart(body1);

            //The picture
            MimeBodyPart imagePart = new MimeBodyPart();

            File outputfile = new File("dobQR.png");
            outputfile.createNewFile();
            if(outputfile.exists()){
            ImageIO.write(image, "png", outputfile);
            }else{
                System.out.println("Can't write");
                return;
            }
            imagePart.attachFile(outputfile);
            imagePart.setContentID("<" + cid + ">");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            body.addBodyPart(imagePart);

            //shove the content into the fodder
            message.setContent(body);
            //and SEND!
            Transport.send(message);
            System.out.println("SENTTTTTTTTTTT");
        } catch (AddressException ex) {
            System.out.println("Không xác định được địa chỉ mail: " + ex.getMessage());
        } catch (MessagingException ex) {
            System.out.println("Lỗi khi gửi tin nhắn: " + ex.getMessage());
        }
    }
    public void sendEmailWithPdfAttachment(String from, String to, String subject, String messageContent, File pdfFile) {
        try {
            //Create a new session
            Session session = Session.getInstance(p, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            //Create a fodder, setting up basic information
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(messageContent);

            MimeBodyPart pdfPart = new MimeBodyPart();
            pdfPart.attachFile(pdfFile);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(pdfPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email with PDF attachment sent successfully.");
        } catch (IOException | MessagingException ex) {
            System.err.println("Error sending email with PDF attachment: " + ex.getMessage());
        }
    }
}
