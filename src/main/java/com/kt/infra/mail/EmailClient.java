package com.kt.infra.mail;

import com.kt.constant.mail.MailTemplate;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailClient {

	private final JavaMailSender mailSender;

	public void sendMail(String email, MailTemplate template, String code) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(
				message,
				true,
				StandardCharsets.UTF_8.name()
			);
			String content = loadHtmlContent();
			if (content != null) {
				content = content.replace("${subject}", template.getSubject());
				content = content.replace("${titleSubject}",
					template == MailTemplate.VERIFY_EMAIL ? "인증 코드" : "임시 비밀번호");
				content = content.replace("${tempCode}", code);
				content = content.replace("${notice}", template.getNotice());
			} else {
				if (template == MailTemplate.VERIFY_EMAIL)
					content = "<div> 인증 코드 : " + code + "</div>";
				else content = "<div> 임시 비밀번호 : " + code + "</div>";
			}

			String sender = ((JavaMailSenderImpl) mailSender).getUsername();
			helper.setFrom(sender);
			helper.setTo(email);
			helper.setSubject(template.getSubject());
			helper.setText(content, true);
			mailSender.send(message);
		} catch(MessagingException exception) {
			throw new IllegalArgumentException("이메일 전송이 실패하였습니다.");
		}

	}

	private String loadHtmlContent() {
		Resource resource = new ClassPathResource("templates/email-template.html");

		try (BufferedReader reader =
					 new BufferedReader(
						 new InputStreamReader(
							 resource.getInputStream(),
							 StandardCharsets.UTF_8
						 )
					 )
		) {
			return reader.lines().collect(
				Collectors.joining("\n")
			);
		} catch (IOException e) {
			return null;
		}
	}

}