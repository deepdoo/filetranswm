/**
 * 
 */
package net.katsu.filebackup.mail;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Session;

import net.katsu.filebackup.utilities.Common;

/**
 * @author b
 *
 */
public class SessionPool {
    private static SessionPool instance = new SessionPool();
    public static SessionPool getInstance(){
        return instance;
    }
    private List<Session> sl = new ArrayList<Session>();
    
    private static int index;

    private SessionPool(){
        List<MailEntity> mailList = Common.getMails();
        for (MailEntity mailEntity : mailList) {
            sl.add(SessionFactory.createSession(mailEntity));
        }
    }
    
    public synchronized Session lookUp() {
            return sl.get(index++%sl.size());
    }
    public void destroy () {
    }
}
