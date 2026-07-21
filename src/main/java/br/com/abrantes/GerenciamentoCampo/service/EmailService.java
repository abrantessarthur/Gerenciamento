
package br.com.abrantes.GerenciamentoCampo.service;

import br.com.abrantes.GerenciamentoCampo.entity.UsuarioEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender enviadorEmail;

    @Value("${spring.mail.username}")
    private String emailOrigem;

    private static final String NOME_ENVIADOR = "Gerenciamento Campo";
    public static final String URL_SITE = "http://localhost:8080";

    public EmailService(JavaMailSender enviadorEmail) {
        this.enviadorEmail = enviadorEmail;
    }

    @Async
    public void enviarEmail(String emailUsuario, String assunto, String conteudo) {
        MimeMessage message = enviadorEmail.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailOrigem, NOME_ENVIADOR);
            helper.setTo(emailUsuario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);

            enviadorEmail.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            throw new RuntimeException("Erro ao enviar e-mail para " + emailUsuario, e);
        }
    }

    public void enviarEmailSenha(UsuarioEntity usuario) {
        String assunto = "Aqui está seu link para alterar a senha";
        String conteudo = gerarConteudoEmail(
                "Olá [[name]],<br><br>"
                        + "Por favor clique no link abaixo para alterar sua senha:<br>"
                        + "<h3><a href=\"[[URL]]\" target=\"_self\">ALTERAR SENHA</a></h3><br>"
                        + "Obrigado,<br>"
                        + "Equipe Gerenciamento Campo.",
                usuario.getNome(),
                URL_SITE + "/auth/redefinir-senha?token=" + usuario.getToken()
        );
        enviarEmail(usuario.getEmail(), assunto, conteudo);
    }

    private String gerarConteudoEmail(String template, String nome, String url) {
        return template.replace("[[name]]", nome).replace("[[URL]]", url);
    }
}
