package pedro.costa.neto.apontamento.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.j256.ormlite.support.ConnectionSource;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import pedro.costa.neto.apontamento.config.DatabaseConfig;
import pedro.costa.neto.apontamento.controller.zoom.EmpresaZoom;
import pedro.costa.neto.apontamento.model.Empresa;
import pedro.costa.neto.apontamento.model.Projeto;
import pedro.costa.neto.apontamento.service.EmpresaService;
import pedro.costa.neto.apontamento.service.ProjetoService;

public class ProjetoFormularioController implements Initializable {
	
	private final static Logger log = Logger.getLogger(ApontamentoListagemController.class);
	
	private ProjetoService projetoService;
	private Projeto projeto;
	
	private EmpresaService empresaService;
	private EmpresaZoom empresaZoom;
	
	@FXML
	private TextField campoEmpresaCodigo;
	
	@FXML
	private TextField campoEmpresaDescricao;
	
	@FXML
	private TextField campoCodigo;
	
	@FXML
	private TextField campoDescricao;
	
	@FXML
	private Button botaoCancelar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		log.info("Iniciando o formulario projeto");
		ConnectionSource connectionSource = null;
		try {
			connectionSource = DatabaseConfig.getConnection();
			projetoService = new ProjetoService(connectionSource);
			empresaService = new EmpresaService(connectionSource);
		}
		catch (Exception e) {
			log.error("Falha ao iniciar o formulario projeto", e);
			e.printStackTrace();
		}
		finally {
			try {
				connectionSource.close();
			} catch (Exception e) {
				log.error("Falha ao fechar a conexao do formulario projeto", e);
				e.printStackTrace();
			}
		}
		
		campoCodigo.focusedProperty().addListener(codigoChange());
		empresaZoom = EmpresaZoom.construir(empresaService, campoEmpresaCodigo, campoEmpresaDescricao);
	}
	
	@FXML
	public void fechar() {
		Stage stage = (Stage) botaoCancelar.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void salvar() {
		preencherDados();
		projetoService.salvar(projeto);
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

	private void preencherDados() {
		if(projeto == null) projeto = new Projeto();
		projeto.setEmpresa(empresaZoom.getValor());
		projeto.setCodigo(campoCodigo.getText());
		projeto.setDescricao(campoDescricao.getText());
	}
	
	private void autoPreenchimentoPorCodigo() {
		String codigo = campoCodigo.getText();
		if(Strings.isNullOrEmpty(codigo)) return;
		
		projeto = projetoService.buscarPorCodigo(codigo);
		if(projeto == null) {
			campoDescricao.setText("");
			campoEmpresaCodigo.setText("");
			campoEmpresaDescricao.setText("");
			return;
		}
		
		log.info("Auto preenchimento dos campos de acordo com o codigo " + codigo);
		
		campoCodigo.setText(projeto.getCodigo());
		campoDescricao.setText(projeto.getDescricao());
		campoEmpresaCodigo.setText(projeto.getEmpresa().getCodigo());
		campoEmpresaDescricao.setText(projeto.getEmpresa().getDescricao());
	}
	
	private ChangeListener<Boolean> codigoChange() {
		return new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(oldValue) autoPreenchimentoPorCodigo();
			}
		};
	}
}
