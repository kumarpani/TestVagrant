package parsers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Objects;

public class ResourcesParser {

  public <T> T getResource(String filePath, Class<T> tClass) {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
    if (Objects.isNull(inputStream)) {
      throw new RuntimeException("Cannot find resources file: " + filePath);
    }
    return GsonParser.toInstance().deserialize(new InputStreamReader(inputStream), tClass);
  }

  public <T> T getResource(String filePath, Type tType) {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
    if (Objects.isNull(inputStream)) {
      throw new RuntimeException("Cannot find resources file: " + filePath);
    }
    return GsonParser.toInstance().deserialize(new InputStreamReader(inputStream), tType);
  }
}
