package apontamento.controller.zoom;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

import apontamento.model.Empresa;
import apontamento.service.EmpresaService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class EmpresaZoom {
	
	private final static Logger log = Logger.getLogger(EmpresaZoom.class);
	
	private EmpresaService empresaService;
	private Empresa empresa;
	
	private TextField campoCodigo;
	private TextField campoDescricao;

	public EmpresaZoom(EmpresaService empresaService, TextField campoCodigo, TextField campoDescricao) {
		this.empresaService = empresaService;
		this.campoCodigo = campoCodigo;
		this.campoDescricao = campoDescricao;
	}

	public Empresa getValor() {
		return empresa;
	}

	private void buscar() {
		String codigo = campoCodigo.getText();
		if(Strings.isNullOrEmpty(codigo)) {
			campoDescricao.setText("");
			return;
		};
		
		empresa = empresaService.buscarPorCodigo(codigo);
		if(empresa == null) {
			campoDescricao.setText("");
			return;
		};
		
		log.info("CODIGO " + codigo);
		
		campoCodigo.setText(empresa.getCodigo());
		campoDescricao.setText(empresa.getDescricao());
	}
	
	private ChangeListener<Boolean> change() {
		return new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(oldValue) buscar();
			}
		};
	}
	
	public static EmpresaZoom construir(EmpresaService empresaService, TextField campoCodigo, TextField campoDescricao) {
		var zoom = new EmpresaZoom(empresaService, campoCodigo, campoDescricao);
		campoCodigo.focusedProperty().addListener(zoom.change());
		return zoom;
	}
}
