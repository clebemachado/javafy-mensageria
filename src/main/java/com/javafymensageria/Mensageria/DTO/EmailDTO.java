package com.javafymensageria.Mensageria.DTO;

import com.javafymensageria.Mensageria.enums.TipoDeMensagem;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmailDTO {

    private String nome;
    private Integer idUsuario;
    private String email;
    private String mensagem;
    private LocalDateTime localDateTime;
    private TipoDeMensagem tipoDeMensagem;
}