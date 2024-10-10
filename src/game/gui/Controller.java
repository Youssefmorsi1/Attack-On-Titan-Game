package game.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

import game.engine.Battle;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.lanes.Lane;
import game.engine.titans.AbnormalTitan;
import game.engine.titans.ArmoredTitan;
import game.engine.titans.ColossalTitan;
import game.engine.titans.PureTitan;
import game.engine.titans.Titan;
import game.engine.weapons.PiercingCannon;
import game.engine.weapons.SniperCannon;
import game.engine.weapons.VolleySpreadCannon;
import game.engine.weapons.WallTrap;
import game.engine.weapons.Weapon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller {
	@FXML public Rectangle Lane1, Lane2, Lane3, Lane4, Lane5;
	@FXML public ImageView Wall1, Wall2, Wall3, Wall4, Wall5;
	@FXML public Text wallHealth1, wallHealth2, wallHealth3, wallHealth4, wallHealth5;
	@FXML public Text wallDanger1, wallDanger2, wallDanger3, wallDanger4, wallDanger5;
	@FXML public VBox wallWeapons1, wallWeapons2, wallWeapons3, wallWeapons4, wallWeapons5;

	
	@FXML public Text scoreDisplay, turnDisplay, phaseDisplay, resourcesDisplay;
	@FXML public AnchorPane pane;
	
	Battle battle;
	int titanSpawnDistance = 540;
	private Stage stage;
	private Scene scene;
	private Parent root;


	public void EasyModeSelect(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("GameScene.fxml"));
            loader.setController(this);  
            root = loader.load();
			stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			
			//initialize
			battle = new Battle(1, 0, titanSpawnDistance, 3, 250);
			LoadTurn(event);
			
			stage.setTitle("Attack on Titan: Utopia");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void HardModeSelect(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("GameScene.fxml"));
            loader.setController(this);  
            root = loader.load();
			stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			
			//initialize
			battle = new Battle(1, 0, titanSpawnDistance, 5, 125);
			LoadTurn(event);

			stage.setTitle("Attack on Titan: Utopia");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	Image pureTitanImage = new Image(getClass().getResourceAsStream("pure.jpg"));
	Image abnormalTitanImage = new Image(getClass().getResourceAsStream("abnormal.jpg"));
	Image armoredTitanImage = new Image(getClass().getResourceAsStream("armored.jpg"));
	Image colossalTitanImage = new Image(getClass().getResourceAsStream("colossal.jpg"));
	ArrayList<ImageView> displayedTitans = new ArrayList<ImageView>();
	ArrayList<Text> displayedHealth = new ArrayList<Text>();
	ArrayList<Text> displayedHeight = new ArrayList<Text>();
	
	public void LoadTurn(ActionEvent event) {
		//set score - turn - phase - resources
		scoreDisplay.setText("Score: " + battle.getScore());
		turnDisplay.setText("Turn: " + battle.getNumberOfTurns());
		phaseDisplay.setText("Phase: " + battle.getBattlePhase());
		resourcesDisplay.setText("Resources: " + battle.getResourcesGathered());
		
		Wall1.setVisible(false);
		Wall2.setVisible(false);
		Wall3.setVisible(false);
		Wall4.setVisible(false);
		Wall5.setVisible(false);
		Lane1.setVisible(false);
		Lane2.setVisible(false);
		Lane3.setVisible(false);
		Lane4.setVisible(false);
		Lane5.setVisible(false);
		wallHealth1.setVisible(false);
		wallHealth2.setVisible(false);
		wallHealth3.setVisible(false);
		wallHealth4.setVisible(false);
		wallHealth5.setVisible(false);
		wallDanger1.setVisible(false);
		wallDanger2.setVisible(false);
		wallDanger3.setVisible(false);
		wallDanger4.setVisible(false);
		wallDanger5.setVisible(false);
		wallWeapons1.setVisible(false);
		wallWeapons2.setVisible(false);
		wallWeapons3.setVisible(false);
		wallWeapons4.setVisible(false);
		wallWeapons5.setVisible(false);
		for(int i = 0; i < displayedTitans.size(); i++) {
			displayedTitans.get(i).setVisible(false);
			displayedHealth.get(i).setVisible(false);
			displayedHeight.get(i).setVisible(false);
		}
		displayedTitans.clear();
		displayedHealth.clear();
		displayedHeight.clear();
		
		//load walls and lanes and check for game over
		ArrayList<Lane> lanes = battle.getOriginalLanes();
		boolean gameOver = true;
		for(int i = 0; i < lanes.size(); i++){
			if(lanes.get(i).isLaneLost() == false)
				gameOver = false;
		}
		
		if(gameOver) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("GAME OVER");
			alert.setHeaderText("Final score = " + battle.getScore());
			alert.setContentText("Do you want to play again ?");
			
			if(alert.showAndWait().get() == ButtonType.OK) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneBuilder1.fxml")); 
	            try {
					root = loader.load();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				
				stage.setTitle("Attack on Titan: Utopia");
				stage.setResizable(false);
				stage.setScene(scene);
				stage.show();
				return;
			}else {
				stage.close();
				return;
			}
		}
		
		
		if(lanes.get(0).isLaneLost() == false) {
			Wall1.setVisible(true);
			Lane1.setVisible(true);
			wallHealth1.setVisible(true);
			wallDanger1.setVisible(true);
			wallHealth1.setText("Health = " + lanes.get(0).getLaneWall().getCurrentHealth());
			wallDanger1.setText("Danger = " + lanes.get(0).getDangerLevel());
			
			int piercingCnt = 0, sniperCnt = 0, volleyCnt = 0, wallCnt = 0;
			ArrayList<Weapon> weapons = lanes.get(0).getWeapons();
			for(Weapon weapon : weapons) {
				if(weapon instanceof PiercingCannon)
					piercingCnt++;
				if(weapon instanceof SniperCannon)
					sniperCnt++;
				if(weapon instanceof VolleySpreadCannon)
					volleyCnt++;
				if(weapon instanceof WallTrap)
					wallCnt++;	
			}
			
			int cnt = 0;
			for (Node node : wallWeapons1.getChildren()) {
			    if (node instanceof Text) {
			        Text text = (Text) node;
			        if(cnt == 0)
			        	text.setText("Piercing Cannon X " + piercingCnt);
			        if(cnt == 1)
			        	text.setText("Sniper Cannon X " + sniperCnt);
			        if(cnt == 2)
			        	text.setText("Volley Cannon X " + volleyCnt);
			        if(cnt == 3)
			        	text.setText("Wall Trap X " + wallCnt);
			    }
			    cnt++;
			}
			wallWeapons1.setVisible(true);
			
			PriorityQueue<Titan> titans = lanes.get(0).getTitans(), tmpTitans = new PriorityQueue<Titan>();
			while(titans.isEmpty() == false) {
				Titan titan = titans.poll();
				tmpTitans.add(titan);
				
				ImageView image = new ImageView();
				Text health = new Text(), height = new Text();
				if(titan instanceof PureTitan)
					image.setImage(pureTitanImage);
				if(titan instanceof AbnormalTitan)
					image.setImage(abnormalTitanImage);
				if(titan instanceof ArmoredTitan)
					image.setImage(armoredTitanImage);
				if(titan instanceof ColossalTitan)
					image.setImage(colossalTitanImage);
				image.setFitWidth(Wall1.getFitWidth());
				image.setFitHeight(Wall1.getFitHeight());
				image.setLayoutX(Wall1.getLayoutX() + titan.getDistance());
				image.setLayoutY(Wall1.getLayoutY());
				health.setLayoutX(image.getLayoutX());
				health.setLayoutY(image.getLayoutY() + 5);
				height.setLayoutX(image.getLayoutX());
				height.setLayoutY(image.getLayoutY() - 5);
				
				health.setText("Health = " + titan.getCurrentHealth());
				health.setFill(Color.GREEN);
				height.setText("Height = " + titan.getHeightInMeters());
				height.setFill(Color.BLUE);
				
				pane.getChildren().add(image);
				pane.getChildren().add(health);
				pane.getChildren().add(height);
				
				displayedTitans.add(image);
				displayedHealth.add(health);
				displayedHeight.add(height);
			}
			
			while(tmpTitans.isEmpty() == false) {
				titans.add(tmpTitans.poll());
			}
			
		}
		
		if(lanes.get(1).isLaneLost() == false) {
			Wall2.setVisible(true);
			Lane2.setVisible(true);
			wallHealth2.setVisible(true);
			wallDanger2.setVisible(true);
			wallHealth2.setText("Health = " + lanes.get(1).getLaneWall().getCurrentHealth());
			wallDanger2.setText("Danger = " + lanes.get(1).getDangerLevel());
			
			int piercingCnt = 0, sniperCnt = 0, volleyCnt = 0, wallCnt = 0;
			ArrayList<Weapon> weapons = lanes.get(1).getWeapons();
			for(Weapon weapon : weapons) {
				if(weapon instanceof PiercingCannon)
					piercingCnt++;
				if(weapon instanceof SniperCannon)
					sniperCnt++;
				if(weapon instanceof VolleySpreadCannon)
					volleyCnt++;
				if(weapon instanceof WallTrap)
					wallCnt++;	
			}
			
			int cnt = 0;
			for (Node node : wallWeapons2.getChildren()) {
			    if (node instanceof Text) {
			        Text text = (Text) node;
			        if(cnt == 0)
			        	text.setText("Piercing Cannon X " + piercingCnt);
			        if(cnt == 1)
			        	text.setText("Sniper Cannon X " + sniperCnt);
			        if(cnt == 2)
			        	text.setText("Volley Cannon X " + volleyCnt);
			        if(cnt == 3)
			        	text.setText("Wall Trap X " + wallCnt);
			    }
			    cnt++;
			}
			wallWeapons2.setVisible(true);
			
			PriorityQueue<Titan> titans = lanes.get(1).getTitans(), tmpTitans = new PriorityQueue<Titan>();
			while(titans.isEmpty() == false) {
				Titan titan = titans.poll();
				tmpTitans.add(titan);
				
				ImageView image = new ImageView();
				Text health = new Text(), height = new Text();
				if(titan instanceof PureTitan)
					image.setImage(pureTitanImage);
				if(titan instanceof AbnormalTitan)
					image.setImage(abnormalTitanImage);
				if(titan instanceof ArmoredTitan)
					image.setImage(armoredTitanImage);
				if(titan instanceof ColossalTitan)
					image.setImage(colossalTitanImage);
				image.setFitWidth(Wall2.getFitWidth());
				image.setFitHeight(Wall2.getFitHeight());
				image.setLayoutX(Wall2.getLayoutX() + titan.getDistance());
				image.setLayoutY(Wall2.getLayoutY());
				health.setLayoutX(image.getLayoutX());
				health.setLayoutY(image.getLayoutY() + 5);
				height.setLayoutX(image.getLayoutX());
				height.setLayoutY(image.getLayoutY() - 5);
				
				health.setText("Health = " + titan.getCurrentHealth());
				health.setFill(Color.GREEN);
				height.setText("Height = " + titan.getHeightInMeters());
				height.setFill(Color.BLUE);
				
				pane.getChildren().add(image);
				pane.getChildren().add(health);
				pane.getChildren().add(height);
				
				displayedTitans.add(image);
				displayedHealth.add(health);
				displayedHeight.add(height);
			}
			
			while(tmpTitans.isEmpty() == false) {
				titans.add(tmpTitans.poll());
			}
			
		}
		
		if(lanes.get(2).isLaneLost() == false) {
			Wall3.setVisible(true);
			Lane3.setVisible(true);
			wallHealth3.setVisible(true);
			wallDanger3.setVisible(true);
			wallHealth3.setText("Health = " + lanes.get(2).getLaneWall().getCurrentHealth());
			wallDanger3.setText("Danger = " + lanes.get(2).getDangerLevel());
			
			int piercingCnt = 0, sniperCnt = 0, volleyCnt = 0, wallCnt = 0;
			ArrayList<Weapon> weapons = lanes.get(2).getWeapons();
			for(Weapon weapon : weapons) {
				if(weapon instanceof PiercingCannon)
					piercingCnt++;
				if(weapon instanceof SniperCannon)
					sniperCnt++;
				if(weapon instanceof VolleySpreadCannon)
					volleyCnt++;
				if(weapon instanceof WallTrap)
					wallCnt++;	
			}
			
			int cnt = 0;
			for (Node node : wallWeapons3.getChildren()) {
			    if (node instanceof Text) {
			        Text text = (Text) node;
			        if(cnt == 0)
			        	text.setText("Piercing Cannon X " + piercingCnt);
			        if(cnt == 1)
			        	text.setText("Sniper Cannon X " + sniperCnt);
			        if(cnt == 2)
			        	text.setText("Volley Cannon X " + volleyCnt);
			        if(cnt == 3)
			        	text.setText("Wall Trap X " + wallCnt);
			    }
			    cnt++;
			}
			wallWeapons3.setVisible(true);
			
			PriorityQueue<Titan> titans = lanes.get(2).getTitans(), tmpTitans = new PriorityQueue<Titan>();
			while(titans.isEmpty() == false) {
				Titan titan = titans.poll();
				tmpTitans.add(titan);
				
				ImageView image = new ImageView();
				Text health = new Text(), height = new Text();
				if(titan instanceof PureTitan)
					image.setImage(pureTitanImage);
				if(titan instanceof AbnormalTitan)
					image.setImage(abnormalTitanImage);
				if(titan instanceof ArmoredTitan)
					image.setImage(armoredTitanImage);
				if(titan instanceof ColossalTitan)
					image.setImage(colossalTitanImage);
				image.setFitWidth(Wall3.getFitWidth());
				image.setFitHeight(Wall3.getFitHeight());
				image.setLayoutX(Wall3.getLayoutX() + titan.getDistance());
				image.setLayoutY(Wall3.getLayoutY());
				health.setLayoutX(image.getLayoutX());
				health.setLayoutY(image.getLayoutY() + 5);
				height.setLayoutX(image.getLayoutX());
				height.setLayoutY(image.getLayoutY() - 5);
				
				health.setText("Health = " + titan.getCurrentHealth());
				health.setFill(Color.GREEN);
				height.setText("Height = " + titan.getHeightInMeters());
				height.setFill(Color.BLUE);
				
				pane.getChildren().add(image);
				pane.getChildren().add(health);
				pane.getChildren().add(height);
				
				displayedTitans.add(image);
				displayedHealth.add(health);
				displayedHeight.add(height);
			}
			
			while(tmpTitans.isEmpty() == false) {
				titans.add(tmpTitans.poll());
			}
			
		}
		
		if(3 < lanes.size() && lanes.get(3).isLaneLost() == false) {
			Wall4.setVisible(true);
			Lane4.setVisible(true);
			wallHealth4.setVisible(true);
			wallDanger4.setVisible(true);
			wallHealth4.setText("Health = " + lanes.get(3).getLaneWall().getCurrentHealth());
			wallDanger4.setText("Danger = " + lanes.get(3).getDangerLevel());
			
			int piercingCnt = 0, sniperCnt = 0, volleyCnt = 0, wallCnt = 0;
			ArrayList<Weapon> weapons = lanes.get(3).getWeapons();
			for(Weapon weapon : weapons) {
				if(weapon instanceof PiercingCannon)
					piercingCnt++;
				if(weapon instanceof SniperCannon)
					sniperCnt++;
				if(weapon instanceof VolleySpreadCannon)
					volleyCnt++;
				if(weapon instanceof WallTrap)
					wallCnt++;	
			}
			
			int cnt = 0;
			for (Node node : wallWeapons4.getChildren()) {
			    if (node instanceof Text) {
			        Text text = (Text) node;
			        if(cnt == 0)
			        	text.setText("Piercing Cannon X " + piercingCnt);
			        if(cnt == 1)
			        	text.setText("Sniper Cannon X " + sniperCnt);
			        if(cnt == 2)
			        	text.setText("Volley Cannon X " + volleyCnt);
			        if(cnt == 3)
			        	text.setText("Wall Trap X " + wallCnt);
			    }
			    cnt++;
			}
			wallWeapons4.setVisible(true);
			
			PriorityQueue<Titan> titans = lanes.get(3).getTitans(), tmpTitans = new PriorityQueue<Titan>();
			while(titans.isEmpty() == false) {
				Titan titan = titans.poll();
				tmpTitans.add(titan);
				
				ImageView image = new ImageView();
				Text health = new Text(), height = new Text();
				if(titan instanceof PureTitan)
					image.setImage(pureTitanImage);
				if(titan instanceof AbnormalTitan)
					image.setImage(abnormalTitanImage);
				if(titan instanceof ArmoredTitan)
					image.setImage(armoredTitanImage);
				if(titan instanceof ColossalTitan)
					image.setImage(colossalTitanImage);
				image.setFitWidth(Wall4.getFitWidth());
				image.setFitHeight(Wall4.getFitHeight());
				image.setLayoutX(Wall4.getLayoutX() + titan.getDistance());
				image.setLayoutY(Wall4.getLayoutY());
				health.setLayoutX(image.getLayoutX());
				health.setLayoutY(image.getLayoutY() + 5);
				height.setLayoutX(image.getLayoutX());
				height.setLayoutY(image.getLayoutY() - 5);
				
				health.setText("Health = " + titan.getCurrentHealth());
				health.setFill(Color.GREEN);
				height.setText("Height = " + titan.getHeightInMeters());
				height.setFill(Color.BLUE);
				
				pane.getChildren().add(image);
				pane.getChildren().add(health);
				pane.getChildren().add(height);
				
				displayedTitans.add(image);
				displayedHealth.add(health);
				displayedHeight.add(height);
			}
			
			while(tmpTitans.isEmpty() == false) {
				titans.add(tmpTitans.poll());
			}
			
		}
		
		if(4 < lanes.size() && lanes.get(4).isLaneLost() == false) {
			Wall5.setVisible(true);
			Lane5.setVisible(true);
			wallHealth5.setVisible(true);
			wallDanger5.setVisible(true);
			wallHealth5.setText("Health = " + lanes.get(4).getLaneWall().getCurrentHealth());
			wallDanger5.setText("Danger = " + lanes.get(4).getDangerLevel());
			
			int piercingCnt = 0, sniperCnt = 0, volleyCnt = 0, wallCnt = 0;
			ArrayList<Weapon> weapons = lanes.get(4).getWeapons();
			for(Weapon weapon : weapons) {
				if(weapon instanceof PiercingCannon)
					piercingCnt++;
				if(weapon instanceof SniperCannon)
					sniperCnt++;
				if(weapon instanceof VolleySpreadCannon)
					volleyCnt++;
				if(weapon instanceof WallTrap)
					wallCnt++;	
			}
			
			int cnt = 0;
			for (Node node : wallWeapons5.getChildren()) {
			    if (node instanceof Text) {
			        Text text = (Text) node;
			        if(cnt == 0)
			        	text.setText("Piercing Cannon X " + piercingCnt);
			        if(cnt == 1)
			        	text.setText("Sniper Cannon X " + sniperCnt);
			        if(cnt == 2)
			        	text.setText("Volley Cannon X " + volleyCnt);
			        if(cnt == 3)
			        	text.setText("Wall Trap X " + wallCnt);
			    }
			    cnt++;
			}
			wallWeapons5.setVisible(true);
			
			PriorityQueue<Titan> titans = lanes.get(4).getTitans(), tmpTitans = new PriorityQueue<Titan>();
			while(titans.isEmpty() == false) {
				Titan titan = titans.poll();
				tmpTitans.add(titan);
				
				ImageView image = new ImageView();
				Text health = new Text(), height = new Text();
				if(titan instanceof PureTitan)
					image.setImage(pureTitanImage);
				if(titan instanceof AbnormalTitan)
					image.setImage(abnormalTitanImage);
				if(titan instanceof ArmoredTitan)
					image.setImage(armoredTitanImage);
				if(titan instanceof ColossalTitan)
					image.setImage(colossalTitanImage);
				image.setFitWidth(Wall5.getFitWidth());
				image.setFitHeight(Wall5.getFitHeight());
				image.setLayoutX(Wall5.getLayoutX() + titan.getDistance());
				image.setLayoutY(Wall5.getLayoutY());
				health.setLayoutX(image.getLayoutX());
				health.setLayoutY(image.getLayoutY() + 5);
				height.setLayoutX(image.getLayoutX());
				height.setLayoutY(image.getLayoutY() - 5);
				
				health.setText("Health = " + titan.getCurrentHealth());
				health.setFill(Color.GREEN);
				height.setText("Height = " + titan.getHeightInMeters());
				height.setFill(Color.BLUE);
				
				pane.getChildren().add(image);
				pane.getChildren().add(health);
				pane.getChildren().add(height);
				
				displayedTitans.add(image);
				displayedHealth.add(health);
				displayedHeight.add(height);
			}
			
			while(tmpTitans.isEmpty() == false) {
				titans.add(tmpTitans.poll());
			}
			
		}
		
	}
	
	@FXML public RadioButton laneButton1, laneButton2, laneButton3, laneButton4, laneButton5;
	@FXML public RadioButton piercingButton, sniperButton, volleyButton, wallButton;
	public void OpenWeaponShop(ActionEvent event) {
        try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("WeaponShop.fxml"));
	        loader.setController(this);
	        Parent weaponShopRoot = loader.load();
			Scene weaponShopScene = new Scene(weaponShopRoot);
			
			stage.setScene(weaponShopScene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ReturnToGame(ActionEvent event) {
		stage.setScene(scene);
		stage.show();
	}
	
	public void BuyWeapon(ActionEvent event) {
		//get Lane and weapon code
		int weaponCode = -1;
		if(piercingButton.isSelected())
			weaponCode = PiercingCannon.WEAPON_CODE;
		else if(sniperButton.isSelected())
			weaponCode = SniperCannon.WEAPON_CODE;
		else if(volleyButton.isSelected())
			weaponCode = VolleySpreadCannon.WEAPON_CODE;
		else if(wallButton.isSelected())
			weaponCode = WallTrap.WEAPON_CODE;
		else
			return;
		
		ArrayList<Lane> tmp = battle.getOriginalLanes();
		int index = -1;
		if(laneButton1.isSelected())
			index = 0;
		else if(laneButton2.isSelected())
			index = 1;
		else if(laneButton3.isSelected())
			index = 2;
		else if(laneButton4.isSelected())
			index = 3;
		else if(laneButton5.isSelected())
			index = 4;
		else
			return;
		
		if(index >= tmp.size())
			return;
		
		try {
			battle.purchaseWeapon(weaponCode, tmp.get(index));
			
			LoadTurn(event);
			stage.setScene(scene);
			stage.show();
		} catch (InsufficientResourcesException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Can't buy weapon");
			alert.setContentText(e.getMessage());
			
			alert.showAndWait();
		} catch (InvalidLaneException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Can't buy weapon");
			alert.setContentText(e.getMessage());
			
			alert.showAndWait();
		}
	}
	
	public void PassTurn(ActionEvent event) {
		battle.passTurn();
		LoadTurn(event);
	}
}
