package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WithMockUser
class MedicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MedicoRepository repository;

    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;

    @Test
    @DisplayName("Deve retornar status HTTP 201 caso os dados sejam validos")
    void cadastrarCenario1() throws Exception {
        var dadosCadastro = new DadosCadastroMedico("Medico", "medico@voll.med", "123456", "61999999999", Especialidade.CARDIOLOGIA, dadosEndereco());
        var medico = new Medico(dadosCadastro);
        var dadosDetalhamento = new DadosDetalhamentoMedico(medico);


        when(repository.save(medico)).thenReturn(medico);

        var response = mvc.perform(
                post("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                dadosCadastroMedicoJson.write(dadosCadastro)
                                        .getJson()
                        )
        ).andReturn().getResponse();

        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamento).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deve retornar status HTTP 400 caso os dados sejam invalidos")
    void cadastrarCenario2() throws Exception {
        var response = mvc.perform(post("/medicos"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}