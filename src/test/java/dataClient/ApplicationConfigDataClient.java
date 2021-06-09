package dataClient;

import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import parsers.ResourcesParser;

import java.lang.reflect.Type;
import java.util.List;

import static constants.SystemProperties.ENV;

public class ApplicationConfigDataClient {

  private final ResourcesParser resourceParser;

  @Inject
  public ApplicationConfigDataClient(ResourcesParser resourceParser) {
    this.resourceParser = resourceParser;
  }

  public <T> T get(String fileName, Class<T> tClass) {
    return resourceParser.getResource(
        String.format("applicationConfig/%s/%s.json", ENV, fileName), tClass);
  }

  public <T> T get(String fileName, Type tType) {
    return resourceParser.getResource(
        String.format("applicationConfig/%s/%s.json", ENV, fileName), tType);
  }

  public List<String> getList(String fileName) {
    return resourceParser.getResource(
        String.format("applicationConfig/%s/%s.json", ENV, fileName),
        new TypeToken<List<String>>() {}.getType());
  }
}
