package Desafio_Tecnico.API_transacao.DTO;

import java.time.OffsetTime;

public class TransacaoDTO
{
    private double valor;
    private OffsetTime datahora;

    public TransacaoDTO(){}

    public TransacaoDTO(double valor, OffsetTime dataHora) {
        this.valor = valor;
        this.datahora = dataHora;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public OffsetTime getDataHora() {
        return datahora;
    }

    public void setDataHora(OffsetTime dataHora) {
        this.datahora = dataHora;
    }

}
