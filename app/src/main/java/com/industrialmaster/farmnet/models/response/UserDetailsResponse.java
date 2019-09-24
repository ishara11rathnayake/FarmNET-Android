package com.industrialmaster.farmnet.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsResponse {

    @SerializedName("_id")
    private String userId;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("profileImage")
    private String profilePicUrl;

    @SerializedName("user_type")
    private String userType;

    @SerializedName("address")
    private String address;

    @SerializedName("contactNumber")
    private String contactNumber;

    @SerializedName("nic")
    private String nic;

    @SerializedName("dob")
    private Date dob;

}
