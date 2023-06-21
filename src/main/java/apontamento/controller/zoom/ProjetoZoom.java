package apontamento.controller.zoom;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

import apontamento.model.Projeto;
import apontamento.service.ProjetoService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class ProjetoZoom {
	
	private final static Logger log = Logger.getLogger(ProjetoZoom.class);
	
	private ProjetoService projetoService;
	private Projeto projeto;
	
	private TextField campoCodigo;
	private TextField campoDescricao;
	private TextField campoEmpresaCodigo;

	public ProjetoZoom(ProjetoService projetoService, TextField campoEmpresaCodigo, TextField campoCodigo, TextField campoDescricao) {
		this.projetoService = projetoService;
		this.campoEmpresaCodigo = campoEmpresaCodigo;
		this.campoCodigo = campoCodigo;
		this.campoDescricao = campoDescricao;
	}
	
	public Projeto getValor() {
		return projeto;
	}

	private void buscar() {
		String codigoProjeto = campoCodigo.getText();
		String codigoEmpresa = campoEmpresaCodigo.getText();
		if(Strings.isNullOrEmpty(codigoProjeto) || Strings.isNullOrEmpty(codigoEmpresa)) {
			campoDescricao.setText("");
			return;
		};
		
		projeto = projetoService.buscarPorEmpresaECodigo(codigoEmpresa, codigoProjeto);
		if(projeto == null) {
			campoDescricao.setText("");
			return;
		};
		
		log.info("CODIGO " + codigoProjeto);
		
		campoCodigo.setText(projeto.getCodigo());
		campoDescricao.setText(projeto.getDescricao());
	}
	
	private ChangeListener<Boolean> change() {
		return new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(oldValue) buscar();
			}
		};
	}
	
	public static ProjetoZoom construir(ProjetoService projetoService, TextField campoEmpresaCodigo, TextField campoCodigo, TextField campoDescricao) {
		var zoom = new ProjetoZoom(projetoService, campoEmpresaCodigo, campoCodigo, campoDescricao);
		campoCodigo.focusedProperty().addListener(zoom.change());
		return zoom;
	}
}
