package study.course.VaadinStudy.services;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Email;
import study.course.VaadinStudy.repository.EmailRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private static final String EMAIL_DE_ORIGEM = "vaadin@ecommerce.com.br";

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarEmail(String subject, String content, String recipient)  {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            Map<String, String> headers = new HashMap<>();
            headers.put("X-Custom-Header", "customHeader");
            headers.put("X-Company-ID", "12345");

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                mimeMessage.addHeader(entry.getKey(), entry.getValue());
            }

            mimeMessage.setSubject(subject);
            mimeMessage.setText(content);
            mimeMessage.addRecipients(Message.RecipientType.TO, recipient);
            mimeMessage.setFrom(EMAIL_DE_ORIGEM);
            javaMailSender.send(mimeMessage);

            Email email = new Email();
            email.setOrigem(EMAIL_DE_ORIGEM);
            email.setConteudo(content);
            email.setDestinatario(recipient);
            email.setEnviadoData(LocalDateTime.now());
            email.setHeaders(headers);

            emailRepository.save(email);
        } catch (MessagingException e){
            System.out.println("Erro ao fazer o envio de email " + e.getMessage());
        }

    }

}
