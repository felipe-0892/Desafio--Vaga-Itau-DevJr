package Desafio_Tecnico.API_transacao.controller;


import Desafio_Tecnico.API_transacao.DTO.ResponseDTO;
import Desafio_Tecnico.API_transacao.model.Transacao;
import Desafio_Tecnico.API_transacao.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController
{
    private final TransacaoService service;

    @Autowired
    public TransacaoController(TransacaoService service) {
        this.service = service;
    }
      //Caso queira receber apenas uma transação
//    @PostMapping
//    public ResponseEntity<ResponseDTO> receberTransacao(@RequestBody Transacao transacao)
//    {
//        if (service.validarTransacao(transacao)) {
//            service.salvarTransacao(transacao);
//
//            // Retorna a resposta com status 201 e a mensagem de sucesso
//            ResponseDTO response = new ResponseDTO("Transação criada com sucesso!", "201 Created");
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        }
//
//        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
//    }


    //Caso queira receber mais de uma transação
    @PostMapping
    public ResponseEntity<ResponseDTO> receberTransacoes(@RequestBody List<Transacao> transacoes) {
        // Validar e salvar cada transação
        for (Transacao transacao : transacoes) {
            if (service.validarTransacao(transacao)) {
                service.salvarTransacao(transacao);
            } else {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }
        }

        // Retorna uma resposta indicando sucesso
        ResponseDTO response = new ResponseDTO("Transações criadas com sucesso!", "201 Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ListagemTransacoesDTO>> listarTransacoes() {
        List<Transacao> transacoes = service.listarTodasTransacoes();

        String mensagem = transacoes.isEmpty() ? "Não há transações cadastradas." : "Transações listadas com sucesso.";


        List<ListagemTransacoesDTO> response = new ArrayList<>();
        response.add(new ListagemTransacoesDTO(mensagem, transacoes));


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<EstatisticaDTO> obterEstatisticas()
    {
        DoubleSummaryStatistics stats = service.calcularEstatisticas();
        return ResponseEntity.ok(new EstatisticaDTO(
                stats.getCount(),
                stats.getSum(),
                stats.getAverage(),
                stats.getMin(),
                stats.getMax()
        ));
    }

    @DeleteMapping
    public  ResponseEntity<Void> limparTransacoes()
    {
        service.limparTransacoes();
        return ResponseEntity.ok().build();
    }


    record EstatisticaDTO(
            long count,
            double sum,
            double avg,
            double min,
            double max
    ){}

    public static class ListagemTransacoesDTO {
        private String mensagem;
        private List<Transacao> transacoes;

        public ListagemTransacoesDTO(String mensagem, List<Transacao> transacoes) {
            this.mensagem = mensagem;
            this.transacoes = transacoes;
        }

        // Getters e Setters
        public String getMensagem() {
            return mensagem;
        }

        public void setMensagem(String mensagem) {
            this.mensagem = mensagem;
        }

        public List<Transacao> getTransacoes() {
            return transacoes;
        }

        public void setTransacoes(List<Transacao> transacoes) {
            this.transacoes = transacoes;
        }
    }
}
