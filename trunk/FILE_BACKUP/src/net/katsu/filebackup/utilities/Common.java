package net.katsu.filebackup.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.jar.Attributes;

import javax.print.DocFlavor.READER;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.katsu.filebackup.mail.MailEntity;
import net.katsu.filebackup.mail.Sender;
import net.katsu.filebackup.mail.SessionPool;

public class Common {
    private static boolean active = false;
    private static boolean isRun = false;

	public static String getProperty(Key key) {
		ResourceBundle rb = ResourceBundle.getBundle("configuration");
		return rb.getString(key.toString());
		
	}
	public enum Key{
        ATT_FOLDER_PATH("AttFolderPath"),
        SENDER_USERNAME("SenderUserName"),
        SENDER_PASSWORD("SenderPassWord"),
        SUBJECT_PREFIX("SubjectPrefix"),
        MAILTO_ADDRESS("MailToAddress"),
        ACCOUNT_FILE_PATH("AccountFilePath");
	    

        /** プロパティキー */
        private String key_;
	    private Key(String key) {
	        this.key_ = key;
        }
	    @Override
	    public String toString() {
	        return this.key_;
	    }
	};
	
	public static void main(String[] args) {
	    
	    BufferedReader bfr = null;
	    try {
	        bfr = new BufferedReader(new InputStreamReader(System.in));
	        String input = bfr.readLine();
	        while (true) {
                if ("quit".equalsIgnoreCase(input)) {
                    if (active) {
                        System.out.println("thread is active..");
                    } else {
                        System.out.println("halted.");
                        break;
                    }
                }else if ("active".equalsIgnoreCase(input)) {
                    System.out.println(active);
                }else if ("isRun".equalsIgnoreCase(input)) {
                    System.out.println(isRun);
                }else if ("start".equalsIgnoreCase(input)) {
                    Common.isRun = true;
                    new Thread( new Runnable() {
                        
                        @Override
                        public void run() {
                            Common.active = true;
                            String attFolderPath = Common.getProperty(Key.ATT_FOLDER_PATH);
                            File folder = new File(attFolderPath);
                            File[] files = folder.listFiles();
                            for (int i = 0; i < files.length; i++) {
                                if (isRun) {
                                    if (files[i].isFile()) {
                                        String suffix = files[i].getName().substring(files[i].getName().lastIndexOf(".")-2, files[i].getName().lastIndexOf("."));
                                        Sender.sendMail(SessionPool.getInstance().lookUp(),"file :" + files[i].getName(), Common.getProperty(Key.SUBJECT_PREFIX) + suffix, files[i], Common.getProperty(Key.MAILTO_ADDRESS));
                                    }
                                } else {
                                    break;
                                }
                            }
                            Common.active = false;
                        }
                    }).start();
                    System.out.println("started.");
                }else if ("stop".equalsIgnoreCase(input)) {
                    Common.isRun = false;
                    new Thread( new Runnable() {
                        
                        @Override
                        public void run() {
                            while (true) {
                                if (!active) {
                                    System.out.println("died.");
                                    break;
                                }
                            }
                        }
                    }).start();
                }else {
                    System.out.println("input wrong.");
                }
                input = bfr.readLine();
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (bfr != null) {
                try {
                    bfr.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
	    
	}
	
	public static List<MailEntity> getMails() {
	    
	    DocumentBuilder db;
	    List<MailEntity> mailList = new ArrayList<MailEntity>();
	    Class<MailEntity> clazz = MailEntity.class;
	    Method[] methods = clazz.getMethods();
	    Map<String, Method> attMap = new HashMap<String, Method>();
        for (int k = 0; k < methods.length; k++) {
            Method method = methods[k];
            if (method.getName().startsWith("set")) {

                String filed = method.getName().replaceFirst("set", ""); 
                attMap.put(filed, method);
            }
        }
	    try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = db.parse(Common.class.getResourceAsStream("/account.xml"));
            NodeList nodeList = document.getElementsByTagName(MailEntity.TagName.MAIL.toString());
            for (int i = 0; i < nodeList.getLength(); i++) {
                MailEntity me = new MailEntity();
                mailList.add(me);
                NodeList childList = nodeList.item(i).getChildNodes();
                for (int j = 0; j < childList.getLength(); j++) {
                    if (attMap.containsKey(childList.item(j).getNodeName())) {
                        attMap.get(childList.item(j).getNodeName()).invoke(me, childList.item(j).getTextContent());

                    }
                }
            }
        } catch (ParserConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {}
	    return mailList;
	}
}
