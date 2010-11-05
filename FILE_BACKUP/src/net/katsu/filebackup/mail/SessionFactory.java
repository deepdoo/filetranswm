/**
 * 
 */
package net.katsu.filebackup.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 * @author noodles.wumu
 *
 */
public class SessionFactory {
    public static Session createSession(final MailEntity mailEntity) {

        Properties props = new Properties();
        props.put("mail.smtp.user", mailEntity.getUserId());
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(mailEntity.getUserId(), mailEntity.getPassWord());
            }
        });
        return session;
    }
}
