package parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

public class GsonParser {
  private final Gson gson;

  private GsonParser() {
    this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
  }

  public static GsonParser toInstance() {
    return new GsonParser();
  }

  public <T> T deserialize(InputStreamReader reader, Class<T> tClass) {
    return gson.fromJson(reader, tClass);
  }

  public <T> T deserialize(InputStreamReader reader, Type tType) {
    return gson.fromJson(reader, tType);
  }

}
