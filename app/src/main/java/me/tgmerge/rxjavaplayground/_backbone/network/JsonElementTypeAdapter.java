package me.tgmerge.rxjavaplayground._backbone.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * 让 JsonElement 类型可以序列化/反序列化的 Gson TypeAdapter。
 *
 * 反序列化：由源直接返回 JsonElement，在模型中自由处理；
 * 序列化：返回 JsonElement 本身，直接序列化。
 *
 * 在 model 中，指定 field 类型为 JsonElement 即可。
 * 
 *     class SomeModel extends BaseModel {
 *         private int id;
 *         private String name;
 *         private JsonElement json;
 *
 *         // ...
 *     }
 *
 * See: http://stackoverflow.com/a/28325108/2996355
 */
final public class JsonElementTypeAdapter implements JsonDeserializer<JsonElement>, JsonSerializer<JsonElement> {

    @Override
    public JsonElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return json;
    }

    @Override
    public JsonElement serialize(JsonElement src, Type typeOfSrc, JsonSerializationContext context) {
        return src;
    }
}
