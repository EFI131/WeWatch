package com.efisteiner.wewatch.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IntegerListTypeConverter {
    @TypeConverter
    public List<Integer> stringToIntegerList(String data){
        Gson gson = new Gson();
        if(data == null || data.isEmpty() || data.equals(null)) {
            return new ArrayList<Integer>();
        }

        Type listType = new TypeToken<List<Integer>>(){}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public String integerListToString(List<Integer> someObjects) {
        Gson gson = new Gson();

        return gson.toJson(someObjects);
    }
}
