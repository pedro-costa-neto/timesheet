package pedro.costa.neto.apontamento.controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.j256.ormlite.support.ConnectionSource;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import pedro.costa.neto.apontamento.config.DatabaseConfig;
import pedro.costa.neto.apontamento.controller.zoom.EmpresaZoom;
import pedro.costa.neto.apontamento.controller.zoom.ProjetoZoom;
import pedro.costa.neto.apontamento.model.Apontamento;
import pedro.costa.neto.apontamento.service.ApontamentoService;
import pedro.costa.neto.apontamento.service.EmpresaService;
import pedro.costa.neto.apontamento.service.ProjetoService;
import pedro.costa.neto.apontamento.util.FuncoesUtil;
import pedro.costa.neto.apontamento.util.MascarasFX;

public class ApontamentoFormularioController implements Initializable {
	
	private final static Logger log = Logger.getLogger(ApontamentoListagemController.class);

	private EmpresaService empresaService;
	private ProjetoService projetoService;
	private ApontamentoService apontamentoService;
	
	private Apontamento apontamento = new Apontamento();
	
	private EmpresaZoom empresaZoom;
	private ProjetoZoom projetoZoom;
	
	@FXML
	private TextField campoEmpresaCodigo;
	
	@FXML
	private TextField campoEmpresaDescricao;
	
	@FXML
	private TextField campoProjetoCodigo;
	
	@FXML
	private TextField campoProjetoDescricao;
	
	@FXML
	private TextField campoData;
	
	@FXML
	private TextField campoHoraInicial;
	
	@FXML
	private TextField campoHoraFinal;
	
	@FXML
	private TextArea campoDescricao;
	
	@FXML
	private Button botaoCancelar;

	public void initialize(URL location, ResourceBundle resources) {		
		log.info("Iniciando a listagem de apontamentos");
		ConnectionSource connectionSource = null;
		try {
			connectionSource = DatabaseConfig.getConnection();
			empresaService = new EmpresaService(connectionSource);
			projetoService = new ProjetoService(connectionSource);
			apontamentoService = new ApontamentoService(connectionSource);
		}
		catch (Exception e) {
			log.error("Ao iniciar janela apontamento", e);
			e.printStackTrace();
		}
		finally {
			try {
				connectionSource.close();
			} catch (Exception e) {
				log.error("Falha ao fechar conexao", e);
				e.printStackTrace();
			}
		}
		
		empresaZoom = EmpresaZoom.construir(empresaService, campoEmpresaCodigo, campoEmpresaDescricao);
		projetoZoom = ProjetoZoom.construir(projetoService, campoEmpresaCodigo, campoProjetoCodigo, campoProjetoDescricao);
		
		MascarasFX.mascaraData(campoData);
		MascarasFX.mascaraHora(campoHoraInicial);
		MascarasFX.mascaraHora(campoHoraFinal);
	}

	public void carregarDados(Long idApontamento) {
		apontamento = apontamentoService.buscarPorId(idApontamento);
		
		campoEmpresaCodigo.setText(apontamento.getEmpresa().getCodigo());
		campoEmpresaDescricao.setText(apontamento.getEmpresa().getRazao());
		campoProjetoCodigo.setText(apontamento.getProjeto().getCodigo());
		campoProjetoDescricao.setText(apontamento.getProjeto().getDescricao());
		campoData.setText(FuncoesUtil.converterDateEmString(apontamento.getDataInicial(), "dd/MM/yyyy"));
		campoHoraInicial.setText(FuncoesUtil.formatarData(apontamento.getDataInicial(), "HH:mm"));
		campoHoraFinal.setText(FuncoesUtil.formatarData(apontamento.getDataFinal(), "HH:mm"));
		campoDescricao.setText(apontamento.getDescricao());
	}
	
	@FXML
	public void fechar() {
		Stage stage = (Stage) botaoCancelar.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void salvar() throws SQLException {
		validarDados();
		apontamento = getDados();
		apontamentoService.salvar(apontamento);
		fechar();
	}
	
	@FXML
	public void onKeyPressed(KeyEvent event) {
		switch (event.getCode()) {
		case ESCAPE:
			fechar();
			break;
		default:
			log.info("A tecla " + event.getCode().getName() + " não foi mapeada");
			break;
		}
	}
	
	private Apontamento getDados() {
		if(apontamento == null) apontamento = new Apontamento();
		
		apontamento.setEmpresa(empresaZoom.getValor());
		apontamento.setProjeto(projetoZoom.getValor());
		
		String data = campoData.getText();
		String horaInicial = campoHoraInicial.getText();
		String horaFinal = campoHoraFinal.getText();
		
		LocalDateTime dataHoraInicial = FuncoesUtil.converterStringEmLocalDateTime(data + " " + horaInicial, "dd/MM/yyyy HH:mm");
		LocalDateTime dataHoraFinal = FuncoesUtil.converterStringEmLocalDateTime(data + " " + horaFinal, "dd/MM/yyyy HH:mm");
		
		Date dataInicial = FuncoesUtil.converterLocalDateTimeEmDate(dataHoraInicial);
		apontamento.setDataInicial(dataInicial);
		
		Date dataFinal = FuncoesUtil.converterLocalDateTimeEmDate(dataHoraFinal);
		apontamento.setDataFinal(dataFinal);
		
		Long totalMinutos = FuncoesUtil.qtdMinutosEntreDuasDatas(dataInicial, dataFinal);
		apontamento.setTotalMinutos(totalMinutos);
		
		String descricao = campoDescricao.getText();
		apontamento.setDescricao(descricao);
		
		return apontamento;
	}
	
	private void validarDados() {
		String empresa = campoEmpresaCodigo.getText();
		String projeto = campoProjetoCodigo.getText();
		String data = campoData.getText();
		String horaInicial = campoHoraInicial.getText();
		String horaFinal = campoHoraFinal.getText();
		
		List<String> mensagens = new ArrayList<String>();
		
		if(Strings.isNullOrEmpty(empresa.trim())) mensagens.add("empresa");
		if(Strings.isNullOrEmpty(projeto.trim())) mensagens.add("projeto");
		if(Strings.isNullOrEmpty(data.trim())) mensagens.add("data");
		if(Strings.isNullOrEmpty(horaInicial.trim())) mensagens.add("hora inicial");
		if(Strings.isNullOrEmpty(horaFinal.trim())) mensagens.add("hora final");
		
		if(!mensagens.isEmpty()) {
			String mensagem = String.join(", ", mensagens);
			
			if(mensagens.size() > 0) {
				mensagem = "Os campos " + mensagem + " são obrigatórios.";
			}
			else {
				mensagem = "O campo " + mensagem + " é obrigatório.";
			}
		}
	}
}
