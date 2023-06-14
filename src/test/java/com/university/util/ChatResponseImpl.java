package com.university.util;

import com.university.dto.ChatResponse;

public class ChatResponseImpl implements ChatResponse {
    private String id;
    private String title;
    private String filename;

    public ChatResponseImpl(String id, String title, String filename) {
        this.id = id;
        this.title = title;
        this.filename = filename;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getFilename() {
        return filename;
    }
}
