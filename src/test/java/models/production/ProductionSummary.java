package models.production;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductionSummary {
  private int totalDocuments;
  private int totalPages;
  private int numberOfCustodians;
}
