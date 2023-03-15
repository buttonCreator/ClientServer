package com.santa.ThirdProject.util;

import java.time.LocalDateTime;

public class TemplateError {

    private String message;

    private LocalDateTime time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

}
