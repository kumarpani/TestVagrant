package models.production;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BasicInfo {
  private String name;
  private String description;
  private String template;
  private String templateGroup;
}
