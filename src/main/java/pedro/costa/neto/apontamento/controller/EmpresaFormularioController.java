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
import pedro.costa.neto.apontamento.model.Empresa;
import pedro.costa.neto.apontamento.service.EmpresaService;
import pedro.costa.neto.apontamento.util.MascarasFX;

public class EmpresaFormularioController implements Initializable {
	
	private final static Logger log = Logger.getLogger(EmpresaFormularioController.class);
	
	private EmpresaService empresaService;
	private Empresa empresa;
	
	@FXML
	private TextField campoCodigo;
	
	@FXML
	private TextField campoRazao;
	
	@FXML
	private TextField campoFantasia;
	
	@FXML
	private TextField campoCnpj;
	
	@FXML
	private Button botaoCancelar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		log.info("Iniciando o formulario empresa");
		ConnectionSource connectionSource = null;
		try {
			connectionSource = DatabaseConfig.getConnection();
			empresaService = new EmpresaService(connectionSource);
		}
		catch (Exception e) {
			log.error("Falha ao iniciar o formulario empresa", e);
			e.printStackTrace();
		}
		finally {
			try {
				connectionSource.close();
			} catch (Exception e) {
				log.error("Falha ao fechar a conexao do formulario empresa", e);
				e.printStackTrace();
			}
		}
		
		campoCodigo.focusedProperty().addListener(codigoChange());
		MascarasFX.mascaraCNPJ(campoCnpj);
	}
	
	@FXML
	public void fechar() {
		Stage stage = (Stage) botaoCancelar.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void salvar() {
		preencherDados();
		empresaService.salvar(empresa);
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
		if(empresa == null) empresa = new Empresa();
		empresa.setCodigo(campoCodigo.getText());
		empresa.setRazao(campoRazao.getText());
		empresa.setFantasia(campoFantasia.getText());
		empresa.setCnpj(campoCnpj.getText());
	}
	
	private void autoPreenchimentoPorCodigo() {
		String codigo = campoCodigo.getText();
		if(Strings.isNullOrEmpty(codigo)) return;
		
		empresa = empresaService.buscarPorCodigo(codigo);
		if(empresa == null) return;
		
		log.info("Auto preenchimento dos campos de acordo com o codigo " + codigo);
		
		campoCodigo.setText(empresa.getCodigo());
		campoRazao.setText(empresa.getRazao());
		campoFantasia.setText(empresa.getFantasia());
		campoCnpj.setText(empresa.getCnpj());
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
