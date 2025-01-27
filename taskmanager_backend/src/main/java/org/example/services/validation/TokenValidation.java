package org.example.services.validation;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.example.dto.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class TokenValidation {

  @Inject
  JWTParser parser;

  private static final Logger LOGGER = Logger.getLogger(TokenValidation.class.getName());

  private final String key =
      "-----BEGIN PRIVATE KEY-----\n"
          + "MIIEvAIBADANBgkqhkiG9w0BAQEFAASC..."
          + "-----END PRIVATE KEY-----";

  public List<Message> validateToken(final String authHeader) {
    final List<Message> errorList = new ArrayList<>();

    if (authHeader == null || !authHeader.startsWith("Bearer:")) {
      errorList.add(Message.builder().title("Invalid token format").build());
      return errorList;
    }

    final String token = authHeader.substring("Bearer:".length()).trim();


    try {
       final JsonWebToken webToken = parser.verify(token, key);
      if(webToken.getExpirationTime()<=System.currentTimeMillis()){

        errorList.add(Message.builder().title("Token already expired").build());
      }
    } catch (final ParseException e) {
      LOGGER.warning("Token validation failed: " + e.getMessage());
      errorList.add(Message.builder().title("Token validation failed").build());
    }

    return errorList;
  }


}
