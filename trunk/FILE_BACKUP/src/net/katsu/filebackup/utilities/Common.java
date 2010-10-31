package net.katsu.filebackup.utilities;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import net.katsu.filebackup.mail.Receive;
import net.katsu.filebackup.mail.Sender;

public class Common {

	public static String getAttFolderPath() {
		ResourceBundle rb = ResourceBundle.getBundle("configuration");
		return rb.getString("AttFolderPath");
		
	}
	
	public static void main(String[] args) {
		String attFolderPath = Common.getAttFolderPath();
		File folder = new File(attFolderPath);
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {

				Sender.getInstance().sendMail("file :" + files[i].getName(), "[]" + files[i].getName(), files[i]);
			}
		}
	}
}
