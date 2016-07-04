package tw.com.softleader.starter.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

/**
 * Use {@link Gson} to serialize/deserialize JSON with some custom typeAdapter
 * 
 * @author Matt
 *
 */
public class JSON {

	private static class BooleanAdapter implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {

		@Override
		public JsonElement serialize(Boolean arg0, Type arg1, JsonSerializationContext arg2) {
			return new JsonPrimitive(arg0 ? 1 : 0);
		}

		@Override
		public Boolean deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
				throws JsonParseException {
			return arg0.getAsInt() == 1;
		}
	}

	private static Gson gson() {
		BooleanAdapter booleanAdapter = new BooleanAdapter();
		return new GsonBuilder().registerTypeAdapter(Boolean.class, booleanAdapter)
				.registerTypeAdapter(boolean.class, booleanAdapter)
				// .setPrettyPrinting()
				.create();
	}

	public static <T> T from(String json, Class<T> classOfT) throws JsonSyntaxException {
		return gson().fromJson(json, classOfT);
	}

	public static String toString(Object src) {
		return gson().toJson(src);
	}

}
