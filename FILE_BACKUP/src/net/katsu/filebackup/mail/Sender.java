package net.katsu.filebackup.mail;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
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
import javax.mail.util.ByteArrayDataSource;

public class Sender {
	private static final Sender SENDER = new Sender();
	public static Sender getInstance() {
		return Sender.SENDER;
	}

	private Session session;
	private Sender(){
		Properties props = new Properties();
		//TODO
        props.put("mail.smtp.user", "");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
		this.session = Session.getInstance(props, new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication()
	        {
	            return new PasswordAuthentication("userName@gmail.com", "password");
	        }
		});
	}
	public boolean sendMail(String text, String subject, File attFile) {

		MimeMessage msg = new MimeMessage(session);
        try {
        	byte[] attachmentData = null;
        	BufferedInputStream bis = null;
        	FileInputStream fis = null;
        	ByteArrayOutputStream baos = null;
        	try {
        		byte[] buffer = new byte[1024];
        		fis = new FileInputStream(attFile);
        		bis = new BufferedInputStream(fis);
        		baos = new ByteArrayOutputStream();
        		while (bis.read(buffer) != -1) {
					baos.write(buffer);
				}
        		attachmentData = baos.toByteArray();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
					try {
						if (baos!=null) {
						baos.close();
						}

						if (bis!=null) {
						bis.close();
						}

						if (fis!=null) {
						fis.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			msg.setText(text);
	        msg.setSubject(subject);
	        Multipart mp = new MimeMultipart();
	        MimeBodyPart attachment = new MimeBodyPart();
	        attachment.setFileName(attFile.getName());
	        attachment.setContent(attachmentData, "application/" + attFile.getName().substring(attFile.getName().indexOf(".")+1));
	        mp.addBodyPart(attachment);
	        msg.setContent(mp);
	        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(""));
	        Transport.send(msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
