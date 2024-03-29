package at.gdev.clipboard;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_API_TOKEN = "api_token";
    private static final String KEY_SALT = "salt";
    private static final String KEY_KEY = "key";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IV = "iv";
    private static final String KEY_EMPTY = "";
    private static final String KEY_ID = "id";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param username
     * @param fullName
     */
    public void loginUser(int id, String username, String fullName, String apiToken, String salt, String key, String password, String iv) {
        mEditor.putInt(KEY_ID, id);
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_FULL_NAME, fullName);
        mEditor.putString(KEY_API_TOKEN, apiToken);
        mEditor.putString(KEY_SALT, salt);
        mEditor.putString(KEY_PASSWORD, password);
        mEditor.putString(KEY_KEY, key);
        mEditor.putString(KEY_IV, iv);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return mPreferences.getString(KEY_API_TOKEN, KEY_EMPTY).length() > 0;
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setFullName(mPreferences.getString(KEY_FULL_NAME, KEY_EMPTY));
        user.setApiToken(mPreferences.getString(KEY_API_TOKEN, KEY_EMPTY));
        user.setSalt(mPreferences.getString(KEY_SALT, KEY_EMPTY));
        user.setKey(mPreferences.getString(KEY_KEY, KEY_EMPTY));
        user.setPassword(mPreferences.getString(KEY_PASSWORD, KEY_EMPTY));
        user.setIv(mPreferences.getString(KEY_IV, KEY_EMPTY));
        user.setId(mPreferences.getInt(KEY_ID, 0));

        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }

}
