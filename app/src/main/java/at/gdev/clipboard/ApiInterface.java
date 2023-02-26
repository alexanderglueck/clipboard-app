package at.gdev.clipboard;

import at.gdev.clipboard.responses.AttachTokenResponse;
import at.gdev.clipboard.responses.PasteResponse;
import at.gdev.clipboard.responses.PushUrlResponse;
import at.gdev.clipboard.responses.RemoveTokenResponse;
import at.gdev.clipboard.responses.SessionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("devices")
    Call<List<Device>> getDevices(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("attach-token")
    Call<AttachTokenResponse> attachToken(@Header("Authorization") String authHeader, @Field("id") int id, @Field("token") String token);

    @FormUrlEncoded
    @POST("remove-token")
    Call<RemoveTokenResponse> removeToken(@Header("Authorization") String authHeader, @Field("token") String token);

    @FormUrlEncoded
    @POST("session")
    Call<SessionResponse> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("paste")
    Call<PushUrlResponse> pushPaste(@Header("Authorization") String authHeader, @Field("device_id") int deviceId, @Field("content") String content);

    @GET("paste/{paste_id}")
    Call<PasteResponse> fetchPaste(@Header("Authorization") String authHeader, @Path("paste_id") int pasteId);
}
