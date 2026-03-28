package com.spacECE.spaceceedu.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    // Video Library APIs
    @GET("/spacece/SpacTube/api_all.php")
    Call<JsonObject> getAllVideos(@Query("uid") String userId, @Query("type") String type);

    @GET("/spacece/SpacTube/api_extractlike.php")
    Call<JsonObject> getLikes(@Query("uid") String userId, @Query("vid") String videoId);

    @GET("/spacece/SpacTube/api_getComment.php")
    Call<JsonObject> getComments(@Query("uid") String userId, @Query("vid") String videoId);

    @FormUrlEncoded
    @POST("/spacece/SpacTube/api_likeVideo.php")
    Call<JsonObject> likeVideo(@Field("uid") String userId, @Field("vid") String videoId);

    @FormUrlEncoded
    @POST("/spacece/SpacTube/api_dislikeVideo.php")
    Call<JsonObject> dislikeVideo(@Field("vid") String videoId, @Field("uid") String userId);

    @FormUrlEncoded
    @POST("/spacece/SpacTube/api_commentVideo.php")
    Call<JsonObject> commentOnVideo(@Field("uid") String userId, @Field("vid") String videoId, @Field("comment") String comment);

    @GET("/spacece/SpacTube/api_getEachCount.php")
    Call<JsonObject> getEachCount();

    @FormUrlEncoded
    @POST("/spacece/SpacTube/api_UpdateViews.php")
    Call<JsonObject> updateViews(@Field("vid") String videoId);

    @GET("/spacece/SpacTube/api_getAllComments.php")
    Call<JsonObject> getAllComments();

    @GET("/spacece/SpacTube/api_getAllCount.php")
    Call<JsonObject> getAllCount();

    @FormUrlEncoded
    @POST("/spacece/SpacTube/api_deleteComment.php")
    Call<JsonObject> deleteComment(@Field("cid") String commentId);

    // Consultation APIs
    @GET("/spacece/ConsultUs/api_category.php")
    Call<JsonObject> getCategories(@Query("category") String category);

    @GET("/spacece/ConsultUs/api_getconsultant.php")
    Call<JsonObject> getConsultant(@Query("cat") String category);

    @FormUrlEncoded
    @POST
    Call<JsonObject> bookAppointment(@Url String url, @Field("payload") String payload);

    @GET("/spacece/ConsultUs/api_user_appoint.php")
    Call<JsonObject> getUserAppointments();

    @GET("/spacece/ConsultUs/agoracallapi.php")
    Call<JsonObject> agoraCall();

    @GET("/spacece/ConsultUs/api_token.php")
    Call<JsonObject> getToken(@Query("email") String email, @Query("token") String token);

    // Library APIs
    @GET("/spacece/libforsmall/allproductlist.php")
    Call<JsonObject> getAllProducts();

    @FormUrlEncoded
    @POST("/spacece/libforsmall/api_addToCart.php")
    Call<JsonObject> addToCart(@Field("u_id") String userId, @Field("p_id") String productId);

    @GET("/spacece/libforsmall/api_fetchCartProducts.php")
    Call<JsonObject> getCartProducts(@Query("u_id") String userId);

    @FormUrlEncoded
    @POST("/spacece/libforsmall/api_RemoveProductFromCart.php")
    Call<JsonObject> removeFromCart(@Field("u_id") String userId, @Field("p_id") String productId);

    // Space Active APIs
    @GET("/spacece/api/spaceactive_activities.php")
    Call<JsonObject> getActivities();

    @FormUrlEncoded
    @POST("/spacece/spacec_active/api_insertUserActivity.php")
    Call<JsonObject> insertUserActivity(@Field("u_id") String userId, @Field("act_id") String activityId);

    @GET("/spacece/spacec_active/api_fetchWorkdone.php")
    Call<JsonObject> fetchWorkdone();

    // Learn On App APIs
    @GET("/spacece/api/learnonapp_courses.php")
    Call<JsonObject> getCourses();

    @FormUrlEncoded
    @POST("/spacece/api/api_InsertLearnOnCourseData.php")
    Call<JsonObject> insertCourseData(@Field("u_id") String userId, @Field("course_id") String courseId);

    // Authentication APIs
    @FormUrlEncoded
    @POST("/spacece/spacece_auth/register_action.php")
    Call<JsonObject> registerUser(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/spacece/spacece_auth/login_action.php")
    Call<JsonObject> loginUser(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/spacece/spacece_auth/api_userVerification.php")
    Call<JsonObject> verifyUser(@Field("email") String email, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("/spacece/spacece_auth/api_updatePassword.php")
    Call<JsonObject> updatePassword(@Field("email") String email, @Field("password") String password);
}