package models.config;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BrowserConfig {
    private Map<String, Object> desiredCapabilities;
    private List<String> arguments;
    private List<String> extensions;
    private Map<String, Object> preferences;
    private Map<String, Object> experimentalOptions;
}
