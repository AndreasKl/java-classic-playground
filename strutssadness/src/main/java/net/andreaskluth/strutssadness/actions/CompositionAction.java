package net.andreaskluth.strutssadness.actions;

import com.opensymphony.xwork2.ActionSupport;

import lombok.Data;

public class CompositionAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    private MessageStore messageStore;

    private String userName;

    public CompositionAction() {
        // Noop
    }

    public String execute() throws Exception {
        messageStore = new MessageStore();
        return SUCCESS;
    }

    public MessageStore getMessageStore() {
        return messageStore;
    }

    public void setMessageStore(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Data
    public class MessageStore {
        private String message;
    }

}
