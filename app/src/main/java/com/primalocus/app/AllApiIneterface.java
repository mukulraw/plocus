package com.primalocus.app;

import com.primalocus.app.getSurveyPOJO.getSurveyBean;
import com.primalocus.app.getworkPOJO.getWorkBean;
import com.primalocus.app.loginPOJO.loginBean;
import com.primalocus.app.statePOJO.stateBean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

;

public interface AllApiIneterface {

    @Multipart
    @POST("api/login.php")
    Call<loginBean> login(
            @Part("username") String username,
            @Part("password") String password,
            @Part("token") String token
    );

    @Multipart
    @POST("api/add_retail.php")
    Call<loginBean> add_retail(
            @Part("user_id") String user_id,
            @Part("property_type") String property_type,
            @Part("date") String date,
            @Part("property_id") String property_id,
            @Part("latitude") String latitude,
            @Part("longitude") String longitude,
            @Part("data_source") String data_source,
            @Part("state") String state,
            @Part("city") String city,
            @Part("location") String location,
            @Part("landmark") String landmark,
            @Part("address") String address,
            @Part("min_price") String min_price,
            @Part("max_price") String max_price,
            @Part("property_availability") String property_availability,
            @Part("posession") String posession,
            @Part("floor") String floor,
            @Part("unit") String unit,
            @Part("condition_property") String condition_property,
            @Part("chargeable_area") String chargeable_area,
            @Part("covered_area") String covered_area,
            @Part("carpet_area") String carpet_area,
            @Part("partition_lease_area") String partition_lease_area,
            @Part("minimum") String minimum,
            @Part("rent") String rent,
            @Part("security_deposit") String security_deposit,
            @Part("maintenance") String maintenance,
            @Part("commonlumpsum") String commonlumpsum,
            @Part("ceiling") String ceiling,
            @Part("facade") String facade,
            @Part("electricity") String electricity,
            @Part("dgspace") String dgspace,
            @Part("backup") String backup,
            @Part("power") String power,
            @Part("opearational") String opearational,
            @Part("tenant") String tenant,
            @Part("tenant_name") String tenant_name,
            @Part("land_usage") String land_usage,
            @Part("fdf") String fdf,
            @Part("fdc") String fdc,
            @Part("fwo") String fwo,
            @Part("tdf") String tdf,
            @Part("tdc") String tdc,
            @Part("two") String two,
            @Part("mobile") String mobile,
            @Part("secondary_phone") String secondary_phone,
            @Part("owned_by") String owned_by,
            @Part("owner_email") String owner_email,
            @Part("caretaker") String caretaker,
            @Part("caretaker_phone") String caretaker_phone,
            @Part("caretaker_email") String caretaker_email,
            @Part("remarks") String remarks,
            @Part("contact") String contact,
            @Part("approved") String approved,
            @Part("brands") String brands,
            @Part("count") String count,
            @Part MultipartBody.Part file3,
            @Part List<MultipartBody.Part> files
    );

    @Multipart
    @POST("api/add_office.php")
    Call<loginBean> add_office(
            @Part("user_id") String user_id,
            @Part("property_type") String property_type,
            @Part("date") String date,
            @Part("property_id") String property_id,
            @Part("latitude") String latitude,
            @Part("longitude") String longitude,
            @Part("data_source") String data_source,
            @Part("state") String state,
            @Part("city") String city,
            @Part("location") String location,
            @Part("landmark") String landmark,
            @Part("address") String address,
            @Part("min_price") String min_price,
            @Part("max_price") String max_price,
            @Part("floor") String floor,
            @Part("unit") String unit,
            @Part("condition_property") String condition_property,
            @Part("workstations") String workstations,
            @Part("cabins") String cabins,
            @Part("conference") String conference,
            @Part("meeting") String meeting,
            @Part("pantry") String pantry,
            @Part("toilets") String toilets,
            @Part("waiting") String waiting,
            @Part("canteen") String canteen,
            @Part("intetnet") String intetnet,
            @Part("chargeable_area") String chargeable_area,
            @Part("covered_area") String covered_area,
            @Part("carpet_area") String carpet_area,
            @Part("partition_lease_area") String partition_lease_area,
            @Part("minimum") String minimum,
            @Part("rent") String rent,
            @Part("security_deposit") String security_deposit,
            @Part("maintenance") String maintenance,
            @Part("ceiling") String ceiling,
            @Part("facade") String facade,
            @Part("opeartional") String opeartional,
            @Part("lease") String lease,
            @Part("lockin") String lockin,
            @Part("electricity") String electricity,
            @Part("dgspace") String dgspace,
            @Part("backup") String backup,
            @Part("tenant") String tenant,
            @Part("tenant_name") String tenant_name,
            @Part("land_usage") String land_usage,
            @Part("fdf") String fdf,
            @Part("fdc") String fdc,
            @Part("fwo") String fwo,
            @Part("tdf") String tdf,
            @Part("tdc") String tdc,
            @Part("two") String two,
            @Part("mobile") String mobile,
            @Part("secondary_phone") String secondary_phone,
            @Part("owned_by") String owned_by,
            @Part("owner_email") String owner_email,
            @Part("caretaker") String caretaker,
            @Part("caretaker_phone") String caretaker_phone,
            @Part("caretaker_email") String caretaker_email,
            @Part("remarks") String remarks,
            @Part("contact") String contact,
            @Part MultipartBody.Part file3,
            @Part List<MultipartBody.Part> files
    );

    @Multipart
    @POST("api/add_warehouse.php")
    Call<loginBean> add_warehouse(
            @Part("user_id") String user_id,
            @Part("property_type") String property_type,
            @Part("date") String date,
            @Part("property_id") String property_id,
            @Part("latitude") String latitude,
            @Part("longitude") String longitude,
            @Part("data_source") String data_source,
            @Part("state") String state,
            @Part("city") String city,
            @Part("location") String location,
            @Part("address") String address,
            @Part("property_availability") String property_availability,
            @Part("posession") String posession,
            @Part("under_construction") String under_construction,
            @Part("warehouse") String warehouse,
            @Part("construction") String construction,
            @Part("min_price") String min_price,
            @Part("max_price") String max_price,
            @Part("plot") String plot,
            @Part("covered_area") String covered_area,
            @Part("available") String available,
            @Part("partition_lease_area") String partition_lease_area,
            @Part("minimum") String minimum,
            @Part("rent") String rent, //approximate rental
            @Part("security_deposit") String security_deposit,
            @Part("maintenance") String maintenance,
            @Part("eaves") String eaves,
            @Part("centerheight") String centerheight,
            @Part("docks") String docks,
            @Part("plinth") String plinth,
            @Part("plan") String plan,
            @Part("firenoc") String firenoc,
            @Part("safety") String safety,
            @Part("ventilation") String ventilation,
            @Part("insulation") String insulation,
            @Part("leveler") String leveler,
            @Part("tenant") String tenant,
            @Part("tenant_name") String tenant_name,
            @Part("land_usage") String land_usage,
            @Part("agreement") String agreement,
            @Part("flooring") String flooring,
            @Part("fwh") String fwh,
            @Part("large") String large,
            @Part("mobile") String mobile,
            @Part("secondary_phone") String secondary_phone,
            @Part("owned_by") String owned_by,
            @Part("owner_email") String owner_email,
            @Part("caretaker") String caretaker,
            @Part("caretaker_phone") String caretaker_phone,
            @Part("caretaker_email") String caretaker_email,
            @Part("remarks") String remarks,
            @Part("contact") String contact,
            @Part("count") String count,
            @Part("floor") String floor,
            @Part("brands") String brands,
            @Part MultipartBody.Part file3,
            @Part List<MultipartBody.Part> files
    );

    @Multipart
    @POST("api/getSueveys.php")
    Call<getSurveyBean> getSurveyBean(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("api/getWork.php")
    Call<getWorkBean> getWork(
            @Part("user_id") String user_id
    );


    @GET("api/getStates.php")
    Call<stateBean> getStates();

    @Multipart
    @POST("api/getCity.php")
    Call<stateBean> getCity(
            @Part("state") String state
    );
}
