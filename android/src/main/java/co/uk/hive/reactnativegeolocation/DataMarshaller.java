package co.uk.hive.reactnativegeolocation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

class DataMarshaller {

    private final Gson mGson;

    public DataMarshaller(Gson gson) {
        mGson = gson;
    }

    public <T> String marshal(T data) {
        return mGson.toJson(data);
    }

    public <T, C extends List<T>> C unmarshalList(String data, Class<T> listClassType) {
        return mGson.fromJson(data, TypeToken.getParameterized(List.class, listClassType).getType());
    }

}
