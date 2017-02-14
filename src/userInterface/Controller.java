package userInterface;

import edu.bsu.cs222.brattonharrison.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {

    private ToggleGroup buttonGroup = new ToggleGroup();
    public TextField searchTermBox;
    public ScrollPane mostRecentDisplayArea;
    public Label rawChangesText;
    public RadioButton mostRecentButton;
    public RadioButton mostActiveButton;
    public Label redirectDisplay;


    private WikiURL wikiURL;
    private XMLParser parser =  new XMLParser();
    private RevisionList<Revision> revisionList;


    public void makeSearchURL() {
        wikiURL = new WikiURL(searchTermBox.getText());
        if(wikiURL.isValid()){
            triggerSearch();
        }
        else{
            showErrorMessage(wikiURL.getErrorMessage());
        }
    }

    private void showErrorMessage(String message){
        System.out.print(message);
    }

    private void triggerSearch(){
        parser.createDocument(wikiURL.getURL());
        revisionList = parser.getRevisionList();
        activateSelectedSearch();
    }

    private void activateSelectedSearch(){
        redirectDisplay.setText(parser.checkForRedirects());
        if(mostRecentButton.isSelected()){
            displayMostRecentData();
        }
        else {
            displayMostActiveData();
        }
    }

    private void displayMostRecentData(){
        String textToAdd = "";
        if(revisionList.size()==0) {
            rawChangesText.setText("No articles found.");
            return;
        }
        for(int i = 0; i< revisionList.size(); i++){
            textToAdd+=(i+1) + ".) " + revisionList.get(i) + "\n\n";
        }
        rawChangesText.setText(textToAdd);
    }

    private void displayMostActiveData(){
        String textToAdd = "";
        if(revisionList.size()==0) {
            rawChangesText.setText("No articles found.");
            return;
        }
            for(int i = 0; i< revisionList.size(); i++){
            textToAdd+=(i+1) + ".) " + revisionList.get(i) + "\n\n";
        }
        rawChangesText.setText(textToAdd);
    }

    @FXML
    public void onEnter() {
        makeSearchURL();
    }

    public void selectButton(){
        mostRecentButton.setToggleGroup(buttonGroup);
        mostActiveButton.setToggleGroup(buttonGroup);
    }
}
