package ru.notivent.service;

import io.jsonwebtoken.io.Decoders;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/** Service for working with passwords */
@Service
public class PasswordService {
  private static final Integer STRENGTH = 16;

  /**
   * Encrypt password
   *
   * @param password Raw password
   * @return Encrypted password
   */
  public String encrypt(String password) {
    var passwordEncoder = new BCryptPasswordEncoder(STRENGTH);
    return passwordEncoder.encode(password);
  }

  /**
   * Matching passwords
   *
   * @param rawPassword Raw password
   * @param encodedPassword Encrypted password
   * @return Matching result
   */
  public boolean matches(String rawPassword, String encodedPassword) {
    var passwordEncoder = new BCryptPasswordEncoder(STRENGTH);
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  /**
   * Decode string from Base64
   */
  public String decodeBase64(String password) throws IllegalArgumentException {
    if (password == null || password.isBlank()) throw new IllegalArgumentException();
    return new String(Decoders.BASE64.decode(password));
  }

  public String generatePassword() {
    var generator = new PasswordGenerator();

    var lowerCaseChars = EnglishCharacterData.LowerCase;
    var lowerCaseRule = new CharacterRule(lowerCaseChars);
    lowerCaseRule.setNumberOfCharacters(2);

    var upperCaseChars = EnglishCharacterData.UpperCase;
    var upperCaseRule = new CharacterRule(upperCaseChars);
    upperCaseRule.setNumberOfCharacters(2);

    var digitChars = EnglishCharacterData.Digit;
    var digitRule = new CharacterRule(digitChars);
    digitRule.setNumberOfCharacters(2);

    return generator.generatePassword(6, lowerCaseRule, upperCaseRule, digitRule);
  }
}
