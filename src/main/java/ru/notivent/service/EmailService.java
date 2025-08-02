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

  private final JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  String notifyEmail;

  @Async
  public void sendEmail(String email, String newPassword) {
    val resetPasswordEmail = buildResetPasswordEmail(email, newPassword);
    javaMailSender.send(resetPasswordEmail);
  }

  private SimpleMailMessage buildResetPasswordEmail(String email, String newPassword) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(email);
    mailMessage.setSubject("Сброс пароля!");
    mailMessage.setFrom(notifyEmail);
    mailMessage.setText("Новый пароль: <" + newPassword + "> " + "Вы можете изменить текущий пароль в разделе <Настройки>. Отвечать на это письмо не нужно!" );
    return mailMessage;
  }
}
