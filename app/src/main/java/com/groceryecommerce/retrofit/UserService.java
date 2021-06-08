package com.groceryecommerce.retrofit;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST(APIClient.APPEND_URL + "category.php")
    Call<JsonObject> getCat(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "subcategory.php")
    Call<JsonObject> getSubcategory(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "product_Info.php")
    Call<JsonObject> getGetProduct(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "home.php")
    Call<JsonObject> getHome(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "login.php")
    Call<JsonObject> getLogin(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "forgot.php")
    Call<JsonObject> getForgot(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "Change_password.php")
    Call<JsonObject> getPinmatch(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "register.php")
    Call<JsonObject> getRegister(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "Delivery_timeslot.php")
    Call<JsonObject> getTimeslot(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "Payment_Settings.php")
    Call<JsonObject> getpaymentgateway(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "Product_search.php")
    Call<JsonObject> getSearch(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "Update_profile.php")
    Call<JsonObject> updateProfile(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "Place_order.php")
    Call<JsonObject> order(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "Delivery_area.php")
    Call<JsonObject> getArea(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "code.php")
    Call<JsonObject> getCode(@Body JsonObject object);


    @POST(APIClient.APPEND_URL + "Address_list.php")
    Call<JsonObject> getAddress(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "Update_address.php")
    Call<JsonObject> updateAddress(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "Coupon_list.php")
    Call<JsonObject> getCoupuns(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "check_coupon.php")
    Call<JsonObject> CheckCoupun(@Body JsonObject object);


//    ******************************NOT COMPLETED API'S'******************************************

    @POST(APIClient.APPEND_URL + "history.php")
    Call<JsonObject> getHistory(@Body JsonObject object);
    //NOT DONE
    @POST(APIClient.APPEND_URL + "plist.php")
    Call<JsonObject> getPlist(@Body JsonObject object);
    //NOT DONE
    @POST(APIClient.APPEND_URL + "feed.php")
    Call<JsonObject> sendFeed(@Body JsonObject object);
    //NOT DONE
    @POST(APIClient.APPEND_URL + "rate.php")
    Call<JsonObject> rates(@Body JsonObject object);
    //NOT DONE
    @POST(APIClient.APPEND_URL + "noti.php")
    Call<JsonObject> getNoti(@Body JsonObject object);
    //NOT DONE
    @POST(APIClient.APPEND_URL + "ocancle.php")
    Call<JsonObject> getOdercancle(@Body JsonObject object);
    //NOT DONE
    @POST(APIClient.APPEND_URL + "n_read.php")
    Call<JsonObject> readNoti(@Body JsonObject object);
    //NOT DONE
    @POST(APIClient.APPEND_URL + "add_del.php")
    Call<JsonObject> deleteAddress(@Body JsonObject object);


}
