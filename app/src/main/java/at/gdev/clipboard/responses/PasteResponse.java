package at.gdev.clipboard.responses;

import com.google.gson.annotations.SerializedName;

public class PasteResponse {
    @SerializedName("content")
    private String content;

    public PasteResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
