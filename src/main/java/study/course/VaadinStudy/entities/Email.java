package study.course.VaadinStudy.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Entity
@Data
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Map<String, String> headers;

    private LocalDateTime enviadoData;

    private String origem;

    private String destinatario;

    private String assunto;

    private String conteudo;
}
