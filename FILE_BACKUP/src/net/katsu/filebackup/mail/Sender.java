package net.katsu.filebackup.mail;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Sender {
	public static boolean sendMail(Session session, String text, String subject, File attFile, String mailToAdd) {

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
				e.printStackTrace();
			} catch (IOException e) {
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
						e.printStackTrace();
					}
			}
			msg.setText(text);
	        msg.setSubject(subject);
	        Multipart mp = new MimeMultipart();
	        MimeBodyPart attachment = new MimeBodyPart();
	        attachment.setFileName(attFile.getName());
	        attachment.setContent(attachmentData, "application/" + attFile.getName().substring(attFile.getName().lastIndexOf(".")+1));
	        mp.addBodyPart(attachment);
	        msg.setContent(mp);
	        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailToAdd));
	        Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return true;
	}
}
