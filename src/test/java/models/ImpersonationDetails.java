package models;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImpersonationDetails {
    private String impersonateTo;
    private String domain;
    private String project;
    private String securityGroup;
}
