package com.uet.libraryManagement.APIService;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VolumeResponse {
    @SerializedName("items")
    public List<Volume> items;
}
