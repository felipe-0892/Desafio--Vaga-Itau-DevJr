package Desafio_Tecnico.API_transacao.service;

import Desafio_Tecnico.API_transacao.model.Transacao;
import Desafio_Tecnico.API_transacao.repository.TransacaoRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransacaoService
{
    private final TransacaoRepository repository;

    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }
    public boolean validarTransacao(@NotNull Transacao transacao) {
        return  transacao.getValor() >= 0 &&
                transacao.getDataHora() != null &&
                transacao.getDataHora().compareTo(OffsetTime.now()) <= 0;
    }

    public void salvarTransacao(Transacao transacao)
    {
        repository.salvar(transacao);
    }

    public void limparTransacoes()
    {
        repository.limpar();
    }

    //-------------------------------------------------------------------------

    public DoubleSummaryStatistics calcularEstatisticas()
    {
        OffsetDateTime umMinutoAtras = OffsetDateTime.now().minusSeconds(60);

        List<Transacao> ultimasTransacoes = repository.buscarTodas().stream()
                .filter(t ->t.getDataHora().isAfter(umMinutoAtras.toOffsetTime()))
                .collect(Collectors.toList());

        //.filter(t ->t.getDataHora().isAfter(umMinutoAtras))

        if (ultimasTransacoes.isEmpty())
        {
            return new DoubleSummaryStatistics();
        }

        return ultimasTransacoes.stream().mapToDouble(Transacao::getValor)
                .summaryStatistics();
    }
    



    public List<Transacao> listarTodasTransacoes() {
        return repository.buscarTodas();
    }


}
