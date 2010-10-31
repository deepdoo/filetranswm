/**
 * 
 */
package net.katsu.filebackup.mail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.iap.Protocol;

/**
 * @author b
 *
 */
public class Receive {

	public  void get() {
		ResourceBundle rb = ResourceBundle.getBundle("configuration");
		Properties props = new Properties();
		try {
			props.load(ClassLoader.getSystemResourceAsStream("configuration.properties"));
			System.out.println(System.getProperty("property.file.path"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		props.list(System.out);
		Session session = Session.getDefaultInstance(System.getProperties(), null); 
		try {
			Store store = session.getStore("pop3");
			try {
				store.connect("", "", "");

				Folder[] f = store.getPersonalNamespaces();
				for (int i = 0; i < f.length; i++) {
					System.out.println(f[i].getName());
				}
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new Receive().get();
	}
}
