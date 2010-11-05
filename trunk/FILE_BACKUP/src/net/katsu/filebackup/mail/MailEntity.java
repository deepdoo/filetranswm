package net.katsu.filebackup.mail;

public class MailEntity {

    public enum TagName {
        MAIL("Mail"), USERID("UserId"), PASSWORD("PassWord"), HOST("Host"), SSL("SSL"), PORT("Port");

        private String tagName;

        private TagName(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public String toString() {
            return this.tagName;
        }

    }

    private String userId;

    private String passWord;

    private String host;

    private String SSL;

    private String port;

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the passWord
     */
    public String getPassWord() {
        return passWord;
    }

    /**
     * @param passWord the passWord to set
     */
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the sSL
     */
    public String getSSL() {
        return SSL;
    }

    /**
     * @param sSL the sSL to set
     */
    public void setSSL(String sSL) {
        SSL = sSL;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }
    

}
