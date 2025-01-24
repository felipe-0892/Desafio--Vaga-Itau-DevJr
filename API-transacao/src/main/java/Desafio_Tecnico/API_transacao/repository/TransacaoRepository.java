package Desafio_Tecnico.API_transacao.repository;

import Desafio_Tecnico.API_transacao.model.Transacao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TransacaoRepository
{
    private List<Transacao> transacoes = new ArrayList<>();

    public void salvar(Transacao transacao)
    {
        transacoes.add(transacao);
    }

    public List<Transacao> buscarTodas()
    {
        return new ArrayList<>(transacoes);
    }

    public void limpar()
    {
        transacoes.clear();
    }


}
