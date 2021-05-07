package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="compagnieMinimo"
    private TextField compagnieMinimo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoDestinazione"
    private ComboBox<Airport> cmbBoxAeroportoDestinazione; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessione"
    private Button btnConnessione; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {

    	int x=0;
    	try {
    		x= Integer.parseInt(compagnieMinimo.getText());
    	}catch(NumberFormatException e ) {
    		txtResult.setText("Inserici un valore");
    		return;
    	}
    	model.creaGrafo(x);
    	
    	txtResult.appendText("GRAFO CREATO!\n");
    	txtResult.appendText("# VERTICI: " + model.getNVertici() + "\n");
    	txtResult.appendText("# ARCHI: " + model.getNArchi() + "\n");
    	
    	
    	cmbBoxAeroportoPartenza.getItems().addAll(model.getVertici());
    	cmbBoxAeroportoDestinazione.getItems().addAll(model.getVertici());
    	
    }

    @FXML
    void doTestConnessione(ActionEvent event) {
    	txtResult.clear();
    	Airport a1 = cmbBoxAeroportoPartenza.getValue();
    	if(a1 == null) {
    		txtResult.setText("Seleziona un aeroporto di partenza");
    		return;
    	}
    	
    	Airport a2 = cmbBoxAeroportoDestinazione.getValue();
    	if(a2 == null) {
    		txtResult.setText("Seleziona un aeroporto di destinazione");
    		return;
    	}
    	
    	List<Airport> percorso = model.trovaPercorso(a1, a2);
    	
    	if(percorso == null) {
    		txtResult.setText("I due nodi non sono collegati");
    	} else{
    		txtResult.appendText(a1 + " e " + a2 + " sono collegati dal seguente percorso:\n\n");
    		txtResult.appendText(percorso.toString());
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert compagnieMinimo != null : "fx:id=\"compagnieMinimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbBoxAeroportoDestinazione != null : "fx:id=\"cmbBoxAeroportoDestinazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessione != null : "fx:id=\"btnConnessione\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
