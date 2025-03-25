package med.voll.api.domain.consulta.validacoes.cancelamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorCancelamentoAntecedencia implements ValidadorCancelamentoDeConsulta {

    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosCancelamentoConsulta dados) {
        var antecedenciaMinima = repository.findDataConsultaById(dados.consultaId()).minusHours(24);
        var agora = LocalDateTime.now();

        if (!agora.isBefore(antecedenciaMinima)) {
            throw new ValidacaoException("A consulta só pode ser cancelada com antecedência de 24 horas");
        }
    }

}
