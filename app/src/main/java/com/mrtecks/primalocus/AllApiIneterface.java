package com.mrtecks.primalocus;

import com.mrtecks.primalocus.loginPOJO.loginBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

;

public interface AllApiIneterface {

    @Multipart
    @POST("prima/api/login.php")
    Call<loginBean> login(
            @Part("username") String username,
            @Part("password") String password,
            @Part("token") String token
    );


    /*@Multipart
    @POST("prima/api/verify.php")
    Call<verifyBean> verify(
            @Part("phone") String phone,
            @Part("otp") String otp
    );

    @Multipart
    @POST("prima/api/update_profile.php")
    Call<verifyBean> update(
            @Part("user_id") String user_id,
            @Part("scheme") String scheme,
            @Part("phone") String phone,
            @Part("alternate_phone") String alternate_phone,
            @Part("name") String name,
            @Part("address") String address,
            @Part("city") String city,
            @Part("state") String state,
            @Part("religion") String religion,
            @Part("marital") String marital,
            @Part("gender") String gender,
            @Part("age") String age,
            @Part("pan_number") String pan_number,
            @Part("aadhar_number") String aadhar_number,
            @Part("account_number") String account_number,
            @Part("bank_name") String bank_name,
            @Part("ifsc") String ifsc,
            @Part("branch") String branch,
            @Part("referral") String referral,
            @Part("member1") String member1,
            @Part("member2") String member2,
            @Part("member3") String member3,
            @Part("member4") String member4,
            @Part("member5") String member5,
            @Part("member6") String member6,
            @Part("member7") String member7,
            @Part("member8") String member8,
            @Part("payment_mode") String payment_mode,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3
    );

    @Multipart
    @POST("prima/api/withdraw.php")
    Call<verifyBean> withdraw(
            @Part("user_id") String user_id,
            @Part("name") String name,
            @Part("amount") String amount,
            @Part MultipartBody.Part file3
    );

    @GET("prima/api/getFaq.php")
    Call<faqBean> getFaq();

    @GET("prima/api/getSupport.php")
    Call<faqBean> getSupport();

    @Multipart
    @POST("prima/api/getMembers.php")
    Call<membersBean> getMembers(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("prima/api/addMember.php")
    Call<membersBean> addMember(
            @Part("user_id") String user_id,
            @Part("relation") String relation,
            @Part("name") String name,
            @Part("age") String age
    );*/

}
