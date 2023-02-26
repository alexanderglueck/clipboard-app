package at.gdev.clipboard.responses;

import com.google.gson.annotations.SerializedName;

public class SessionResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("api_token")
    private String apiToken;

    @SerializedName("salt")
    private String salt;

    @SerializedName("key")
    private String key;

    @SerializedName("iv")
    private String iv;

    public SessionResponse(int id, String name, String email, String apiToken, String salt, String key, String iv) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.apiToken = apiToken;
        this.salt = salt;
        this.key = key;
        this.iv = iv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
