package tallerii.stories.network.apimodels;

import android.support.annotation.NonNull;

import tallerii.stories.helpers.DateUtils;

public class ChatMessage {
    public static final String chatIdFormat = "%s_%s";
    private String chatId;
    private String message;
    private String senderId;
    private String receiverId;
    private long timestamp;
 
    public ChatMessage() {
    }
 
    public ChatMessage(String message, String senderId, String receiverId) {
        this.chatId = generateChatId(senderId, receiverId);
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = DateUtils.getNowTime();
    }

    @NonNull
    public static String generateChatId(String senderId, String receiverId) {
        int compare = senderId.compareTo(receiverId);
        if (compare < 0 )
            return String.format(chatIdFormat, senderId, receiverId);
        else
            return String.format(chatIdFormat, receiverId, senderId);
    }

    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
 
    public String getSenderId() {
        return senderId;
    }
 
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
 
    public String getReceiverId() {
        return receiverId;
    }
 
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String obtainDate() {
        return DateUtils.getTimeFromTimestamp(timestamp);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}