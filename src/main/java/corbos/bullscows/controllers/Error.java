package corbos.bullscows.controllers;

import java.time.LocalDateTime;

public class Error {
    
    private LocalDateTime timeStamp = LocalDateTime.now();
    
    private String mess;
    
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
    
    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public String getMessage() {
        return mess;
    }
    
    public void setMessage(String mess) {
        this.mess = mess;
    }
}
