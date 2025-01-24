package Desafio_Tecnico.API_transacao.model;

import java.time.OffsetTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class Transacao
{


    private double valor;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetTime dataHora;

    //getters
    public double getValor() {
        return valor;
    }

    public OffsetTime getDataHora() {
        return dataHora;
    }

    //Setters

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setDataHora(OffsetTime dataHora) {
        this.dataHora = dataHora;
    }
}
