package com.javafymensageria.Mensageria.service;

import com.javafymensageria.Mensageria.DTO.EmailDTO;
import com.javafymensageria.Mensageria.enums.TipoDeMensagem;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final freemarker.template.Configuration fmConfiguration;

    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String from;

    private String mensagem = "";

    public void sendEmail(EmailDTO emailDTO) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(emailDTO.getEmail());

            TipoDeMensagem tipoDeMensagem = emailDTO.getTipoDeMensagem();

            if(tipoDeMensagem.equals(TipoDeMensagem.CREATE)){
                mimeMessageHelper.setSubject("Cadastro realizado");
            }else if (tipoDeMensagem.equals(TipoDeMensagem.UPDATE)){
                mimeMessageHelper.setSubject("Cadastro atualizado");
            }else if (tipoDeMensagem.equals(TipoDeMensagem.BIRTHDAY)){
                mimeMessageHelper.setSubject("Feliz Aniversário!");
            }else{
                mimeMessageHelper.setSubject("Cadastro restringido");
            }
            mimeMessageHelper.setText(geContentFromTemplate(emailDTO, emailDTO.getTipoDeMensagem().getTipoDeMensagem()), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            mensagem = "Error ao enviar o email";
        }
    }

    public String geContentFromTemplate(EmailDTO email, String tipoMensagem) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", email.getNome());
        dados.put("id", email.getIdUsuario());
        dados.put("email", from);
        Template template = null;
        switch (tipoMensagem){
            case "create" -> template = fmConfiguration.getTemplate("email_boas_vindas-template.ftl");
            case "update" -> template = fmConfiguration.getTemplate("email_atualizar_endereco-template.ftl");
            case "birthday" -> template = fmConfiguration.getTemplate("email_aniversario-template.ftl");
            default -> template = fmConfiguration.getTemplate("email_deletar_endereco-template.ftl");}

        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }

    public String getMensagem(){
        return mensagem;
    }

}
