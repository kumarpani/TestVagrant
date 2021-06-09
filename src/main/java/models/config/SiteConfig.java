package models.config;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SiteConfig {
    private String url;
    private String title;
}
