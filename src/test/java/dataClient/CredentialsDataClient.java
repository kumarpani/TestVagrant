package dataClient;

import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import models.Credentials;

import java.util.List;
import java.util.Map;

public class CredentialsDataClient {

  private final Map<String, List<Credentials>> credentialsMap;

  @Inject
  public CredentialsDataClient(ApplicationConfigDataClient applicationConfigDataClient) {
    // TODO:: Implement Cache
    credentialsMap =
        applicationConfigDataClient.get(
            "credentials", new TypeToken<Map<String, List<Credentials>>>() {}.getType());
  }

  public Credentials getProjectAdministrator() {
    return credentialsMap.get("projectAdministrator").stream()
        .findFirst()
        .orElse(new Credentials());
  }

  public Credentials getCredentials(String key) {
    return credentialsMap.get(key).stream().findFirst().orElse(new Credentials());
  }
}
