package pedro.costa.neto.apontamento.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.j256.ormlite.support.ConnectionSource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import pedro.costa.neto.apontamento.config.DatabaseConfig;
import pedro.costa.neto.apontamento.controller.zoom.EmpresaZoom;
import pedro.costa.neto.apontamento.dto.ApontamentoDto;
import pedro.costa.neto.apontamento.dto.ApontamentoFiltroDto;
import pedro.costa.neto.apontamento.model.Apontamento;
import pedro.costa.neto.apontamento.service.ApontamentoService;
import pedro.costa.neto.apontamento.service.EmpresaService;
import pedro.costa.neto.apontamento.util.FuncoesUtil;
import pedro.costa.neto.apontamento.util.MascarasFX;

public class ApontamentoListagemController implements Initializable {

    private final static Logger log = Logger.getLogger(ApontamentoListagemController.class);
    
    private ApontamentoService apontamentoService;
    private EmpresaService empresaService;
    
    @FXML
    private TableView<ApontamentoDto> apontamentos;
    
    @FXML
    private TextField campoEmpresaCodigo;
    
    @FXML
    private TextField campoEmpresaDescricao;
    
    @FXML
    private TextField campoDataInicio;
    
    @FXML
    private TextField campoDataFim;

	public void initialize(URL location, ResourceBundle resources) {
		log.info("Iniciando a listagem de apontamentos");
		ConnectionSource connectionSource = null;
		try {
			iniciarFiltro();
			
			String[][] collumns = {
	            {"Empresa", "empresaDescricao"},
	            {"Projeto", "projetoDescricao"},
	            {"Data", "data"},
	            {"Hora Início", "horaInicial"},
	            {"Hora Fim", "horaFinal"},
	            {"Total", "horaFormatada"},
	            {"Descrição", "descricao"}
	        };

			ObservableList<TableColumn<ApontamentoDto, String>> collumnList = FXCollections.observableArrayList();
			for (String[] column : collumns) {
	            TableColumn<ApontamentoDto, String> tableColumn = new TableColumn<ApontamentoDto, String>(column[0]);
	            tableColumn.setCellValueFactory(new PropertyValueFactory<ApontamentoDto, String>(column[1]));
	            collumnList.add(tableColumn);
	        }
			this.apontamentos.getColumns().addAll(collumnList);
			
			connectionSource = DatabaseConfig.getConnection();
			
			empresaService = new EmpresaService(connectionSource);
			apontamentoService = new ApontamentoService(connectionSource);
	        
			EmpresaZoom.construir(empresaService, campoEmpresaCodigo, campoEmpresaDescricao);
	        
	        MascarasFX.mascaraData(campoDataInicio);
	        MascarasFX.mascaraData(campoDataFim);
	        
	        atualizarTabela();
		}
		catch (Exception e) {
			log.error("Falha ao iniciar a tela de listagem de apontamento", e);
			e.printStackTrace();
		}
		finally {
			try {
				connectionSource.close();
			} catch (Exception e) {
				log.error("Falha ao fechar conexao do banco de dados", e);
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void atualizarTabela() {
		ApontamentoFiltroDto filtro = new ApontamentoFiltroDto();
		filtro.setEmpresaCodigo(campoEmpresaCodigo.getText());
		
		try {
			String dataInicial = campoDataInicio.getText();
			filtro.setDataInicial(FuncoesUtil.converterStringEmDate(dataInicial, "dd/MM/yyyy"));
			
			String dataFinal = campoDataFim.getText();
			filtro.setDataFinal(FuncoesUtil.converterStringEmDate(dataFinal, "dd/MM/yyyy"));
		}
		catch (Exception e) {
			log.error("Falha ao converter data", e);
			e.printStackTrace();
		}
		
		List<Apontamento> apontamentos = apontamentoService.listarPorFiltro(filtro);
		List<ApontamentoDto> apontamentoDtos = ApontamentoDto.construir(apontamentos);
        this.apontamentos.setItems(FXCollections.observableArrayList(apontamentoDtos));
	}

	public void cadastrarApontamento() {
		try {
			URL path = ApontamentoListagemController.class.getResource("/layouts/apontamento_formulario.fxml");
			Parent parent = FXMLLoader.load(path);
	        Scene scene = new Scene(parent);
	        Stage stage = new Stage();
	        
	        stage.setTitle("Cadastrar apontamento");
	        stage.getIcons().add(new Image("/icon.png"));
	        stage.setScene(scene);
	        stage.show();
		}
		catch (IOException e) {
			log.error("Falha ao abrir janela para cadastrar apontamento", e);
			e.printStackTrace();
		}
	}

	public void editarApontamento() {
		try {
			ApontamentoDto apontamentoDto = apontamentos.getSelectionModel().getSelectedItem();
			if(apontamentoDto == null) return;
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/layouts/apontamento_formulario.fxml"));
            Parent root = loader.load();
			ApontamentoFormularioController formController = loader.getController();
			
			formController.carregarDados(apontamentoDto.getId());
			
			
	        Scene scene = new Scene(root);
	        Stage stage = new Stage();
	        
	        stage.setTitle("Editar apontamento");
	        stage.getIcons().add(new Image("/icon.png"));
	        stage.setScene(scene);
	        stage.show();
		}
		catch (IOException e) {
			log.error("Falha ao abrir janela para cadastrar apontamento", e);
			e.printStackTrace();
		}
	}

	public void cadastrarEmpresa() {
		try {
			URL path = ApontamentoListagemController.class.getResource("/layouts/empresa_formulario.fxml");
			Parent parent = FXMLLoader.load(path);
	        Scene scene = new Scene(parent);
	        Stage stage = new Stage();
	        
	        stage.setTitle("Empresa");
	        stage.getIcons().add(new Image("/icon.png"));
	        stage.setResizable(false);
	        stage.setScene(scene);
	        stage.show();
		}
		catch (IOException e) {
			log.error("Falha ao abrir janela para cadastrar empresa", e);
			e.printStackTrace();
		}
	}

	public void cadastrarProjeto() {
		try {
			URL path = ApontamentoListagemController.class.getResource("/layouts/projeto_formulario.fxml");
			Parent parent = FXMLLoader.load(path);
	        Scene scene = new Scene(parent);
	        Stage stage = new Stage();
	        
	        stage.setTitle("Projeto");
	        stage.getIcons().add(new Image("/icon.png"));
	        stage.setResizable(false);
	        stage.setScene(scene);
	        stage.show();
		}
		catch (IOException e) {
			log.error("Falha ao abrir janela para cadastrar projeto", e);
			e.printStackTrace();
		}
	}
	
	@FXML
	public void onKeyPressed(KeyEvent event) {
		switch (event.getCode()) {
		case F1:
			cadastrarApontamento();
			break;
		case ENTER:
			editarApontamento();
			break;
		case F3:
			cadastrarEmpresa();
			break;
		case F4:
			cadastrarProjeto();
			break;
		default:
			log.info("A tecla " + event.getCode().getName() + " não foi mapeada");
			break;
		}
	}
	
	private void iniciarFiltro() {
		campoEmpresaCodigo.setText("");
	    campoEmpresaDescricao.setText("");
	    
	    Date dataInicial =  FuncoesUtil.primeiroDiaDoMesCorrente();
	    Date dataFinal = FuncoesUtil.ultimoDiaDoMesCorrente();
	    
	    try {
		    campoDataInicio.setText(FuncoesUtil.converterDateEmString(dataInicial, "dd/MM/yyyy"));
		    campoDataFim.setText(FuncoesUtil.converterDateEmString(dataFinal, "dd/MM/yyyy"));
	    }
	    catch (Exception e) {
	    	log.error("Falha ao converter data", e);
	    	e.printStackTrace();
		}
	}
}
