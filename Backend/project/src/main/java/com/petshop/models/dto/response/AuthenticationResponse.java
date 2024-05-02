package com.petshop.models.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.*;
import org.hibernate.annotations.NamedNativeQuery;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthenticationResponse {
   @JsonProperty("access_token")
   private String accessToken;
   @JsonProperty("refresh_token")
   private String refresh_token;
}