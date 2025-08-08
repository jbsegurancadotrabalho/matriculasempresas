package br.com.jbst.DTO;



import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.config.InstantSerializer;
import lombok.Data;

@Data
public class MailSenderDto {
    private String mailTo;
    private String subject;
    private String body;
    private byte[] attachment;
    private String attachmentName;

}
