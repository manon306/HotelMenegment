package com.example.NotificationService.Exceptions;

import java.time.LocalDateTime;

public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ApiError(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // getters
    public int getStatus()
    {
        return this.status;
    }
    public String getError()
    {
        return this.error;
    }
    public String getmessage()
    {
        return this.message;
    }
    public String getpath()
    {
        return this.path;
    }
    public LocalDateTime getTimeStamp()
    {
        return this.timestamp;
    }
    //Setters
    public void setStatus(int status)
    {
        this.status=status;
    }
    public void setError(String error)
    {
        this.error=error;
    }
    public void setMessage(String message)
    {
        this.message=message;
    }
    public void setPath(String path)
    {
        this.path=path;
    }

}