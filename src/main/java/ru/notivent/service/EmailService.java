package ru.notivent.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  @Value("${email.confirmation.host}")
  String host;

  @Value("${email.confirmation.from}")
  String from;

  private final JavaMailSender javaMailSender;

  @Async
  public void sendEmail(String email, String newPassword) {
    val resetPasswordEmail = buildResetPasswordEmail(email, newPassword);
    javaMailSender.send(resetPasswordEmail);
  }

  private SimpleMailMessage buildResetPasswordEmail(String email, String newPassword) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(email);
    mailMessage.setSubject("Сброс пароля!");
    mailMessage.setFrom(from);
    mailMessage.setText("Новый пароль: < " + newPassword + " > " + "Вы можете изменить текущий пароль в разделе <Настройки>." );
    return mailMessage;
  }
}
