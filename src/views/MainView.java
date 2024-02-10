package views;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.List;

import com.sun.prism.RTTexture;

import engine.Game;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.characters.*;
import model.collectibles.*;
import model.world.*;
import exceptions.*;

public class MainView extends Application {
	static Hero inControl;
	public static boolean willHeal;
	static Button[][] btns = new Button[15][15];
	static ImageView invis = new ImageView("invisible.jpg");
	static SoundEffect welcomeMenuMusic;
	public static Button attack = new Button("ATTACK");
	public static Button cure = new Button("CURE");
	public static Button useSpecial = new Button("SPECIAL");

	public void start(Stage stage) throws Exception {

		try {
			welcomeMenuMusic = new SoundEffect(
					"C:\\Users\\omarg\\eclipse-workspace\\Team7.zip_expanded\\Team7\\src\\Effect\\ElTanyBayz.mp3");
			welcomeMenuMusic.soundEffect.setCycleCount(MediaPlayer.INDEFINITE);
			welcomeMenuMusic.soundEffect.play();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		stage = new Stage();
		// change the title
		stage.setTitle("The Last Of Us - Legacy");
		// set the logo and add it
		Image logo = new Image("logo.png");
		stage.getIcons().add(logo);
		stage.setScene(spawnWelcomeMenu(stage));
		stage.setFullScreen(true);
		stage.show();
	}

	public static Scene spawnWelcomeMenu(Stage stage) {
		// Create the Welcome menu
		StackPane root = new StackPane();
		EventHandler<MouseEvent> strt = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				// THE FADE OUT OF THE MAIN MENU MUSIC
				// THE TIMELINE BELOW IS THE DURATION OVER WHICH THE MUSIC WILL FADE OUT
				FadeTransition fadeIn = new FadeTransition(Duration.seconds(.7), root);
				fadeIn.setFromValue(0.0);
				fadeIn.setToValue(1.0);
				//
				FadeTransition fadeOut = new FadeTransition(Duration.seconds(.7), root);
				fadeOut.setFromValue(1.0);
				fadeOut.setToValue(0.0);
				fadeOut.setOnFinished(event -> {
					stage.setScene(spawnHeroChoice(stage));
					stage.setFullScreen(true);
					fadeIn.play();
				});
				fadeOut.play();
			}
		};
		EventHandler<MouseEvent> end = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				Platform.exit();
			}
		};

		// create the welcome img
		Image phot = new Image("welcome photo.png");
		ColorAdjust strtAdj = new ColorAdjust();
		ColorAdjust endAdj = new ColorAdjust();
		// create the starting button as an imageview
		Image starting = new Image("startButton.png");
		ImageView startingBut = new ImageView(starting);
		startingBut.fitWidthProperty().bind(root.widthProperty().divide(7));
		startingBut.fitHeightProperty().bind(root.heightProperty().divide(9));
		startingBut.setOnMouseClicked(strt);
		strtAdj.setBrightness(-.7);
		startingBut.setEffect(strtAdj);
		startingBut.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
			strtAdj.setBrightness(0);
			startingBut.setEffect(strtAdj);
		});
		startingBut.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
			strtAdj.setBrightness(-.7);
			startingBut.setEffect(strtAdj);
		});

		// create the exit button as an imageview
		ImageView exitBut = new ImageView("exitButton.png");
		exitBut.fitWidthProperty().bind(root.widthProperty().divide(7));
		exitBut.fitHeightProperty().bind(root.heightProperty().divide(9));
		exitBut.setOnMouseClicked(end);
		endAdj.setBrightness(-.7);
		exitBut.setEffect(endAdj);
		exitBut.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
			endAdj.setBrightness(0);
			exitBut.setEffect(endAdj);
		});
		exitBut.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
			endAdj.setBrightness(-.7);
			exitBut.setEffect(endAdj);
		});
		VBox btns = new VBox();
		btns.getChildren().add(startingBut);
		btns.getChildren().add(exitBut);
		btns.setSpacing(25);
		btns.setPadding(new Insets(15));
		root.getChildren().add(btns);
		btns.setAlignment(Pos.BOTTOM_RIGHT);
		root.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image("welcome Photo.png")),
				new CornerRadii(0), new Insets(0))));
		Scene welcome = new Scene(root);
		return welcome;
	}

	public static Scene spawnHeroChoice(Stage stage) {
		// the Whole scene here
		double x = Screen.getPrimary().getBounds().getWidth();
		double y = Screen.getPrimary().getBounds().getHeight();
		VBox root = new VBox();
		root.setPrefSize(x, y);
		//
		Text data = new Text();
		data.prefWidth(x / 3);
		data.prefHeight(y / 3);
		data.setFill(Color.ANTIQUEWHITE);
		Font myFont = Font.font("Impact", 25);
		data.setFont(myFont);
		// this will hold heros pics
		HBox heros1 = new HBox();
		heros1.setPrefHeight(y / 4);
		heros1.setPrefWidth(x);
		heros1.setSpacing(10);
		heros1.setAlignment(Pos.CENTER);
		HBox heros2 = new HBox();
		heros2.setPrefHeight(y / 4);
		heros2.setPrefWidth(x);
		heros2.setSpacing(10);
		heros2.setAlignment(Pos.CENTER);
		// this will hold the text that says choose
		ImageView choose = new ImageView("chooseYourHero.png");
		choose.prefWidth(x / 3);
		choose.prefHeight(y / 4);
		root.getChildren().add(choose);
		EventHandler<MouseEvent> bck = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				inControl = null;
				Scene home = spawnWelcomeMenu(stage);
				FadeTransition fadeIn = new FadeTransition(Duration.seconds(.7), home.getRoot());
				fadeIn.setFromValue(0.0);
				fadeIn.setToValue(1.0);
				//
				FadeTransition fadeOut = new FadeTransition(Duration.seconds(.7), root);
				fadeOut.setFromValue(1.0);
				fadeOut.setToValue(0.0);
				fadeOut.setOnFinished(event -> {
					stage.setScene(home);
					stage.setFullScreen(true);
					fadeIn.play();
				});
				fadeOut.play();
			}

		};
		EventHandler<MouseEvent> Go = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if (inControl == null) {
					data.setText("CHOOSE A HERO FIRST");
				} else {
					Game.startGame(inControl);
					FadeTransition fadeIn = new FadeTransition(Duration.seconds(.7), root);
					fadeIn.setFromValue(0.0);
					fadeIn.setToValue(1.0);
					//
					FadeTransition fadeOut = new FadeTransition(Duration.seconds(.7), root);
					fadeOut.setFromValue(1.0);
					fadeOut.setToValue(0.0);
					fadeOut.setOnFinished(event -> {
						stage.setScene(spawnMainGame(stage));
						stage.setFullScreen(true);
						fadeIn.play();
					});
					fadeOut.play();
				}
				;
			}
		};
		HBox lowerSection = new HBox();
		lowerSection.setPrefWidth(x);
		lowerSection.setPrefHeight(y / 4);
		lowerSection.setSpacing(30);
		lowerSection.setPadding(new Insets(10));

		ImageView goToGame = new ImageView("Go.png");
		goToGame.fitHeightProperty().bind(lowerSection.heightProperty().divide(2.3));
		goToGame.fitWidthProperty().bind(lowerSection.widthProperty().divide(7));
		goToGame.setOnMouseClicked(Go);
		ColorAdjust b = new ColorAdjust();
		b.setBrightness(-.6);
		goToGame.setEffect(b);
		goToGame.setOnMouseEntered(e -> {
			b.setBrightness(0);
		});
		goToGame.setOnMouseExited(e -> {
			b.setBrightness(-.6);
		});
		//
		ImageView back = new ImageView("Back.png");
		back.fitHeightProperty().bind(lowerSection.heightProperty().divide(2.3));
		back.fitWidthProperty().bind(lowerSection.widthProperty().divide(7));
		back.setOnMouseClicked(bck);
		ColorAdjust k = new ColorAdjust();
		k.setBrightness(-.6);
		back.setEffect(k);
		back.setOnMouseEntered(e -> {
			k.setBrightness(0);
		});
		back.setOnMouseExited(e -> {
			k.setBrightness(-.6);
		});
		// here to load the heroes from the file efficiently
		for (int i = 0; i < 8; i++) {
			HeroView h = new HeroView(Game.availableHeroes.get(i).getName() + ".png");
			if (i < 4) {
				heros1.getChildren().add(h);
			} else {
				heros2.getChildren().add(h);
			}
			h.fitWidthProperty().bind(heros1.widthProperty().divide(7));
			h.fitHeightProperty().bind(heros1.heightProperty().divide(1.1));
			h.setH(Game.availableHeroes.get(i));
			ColorAdjust adj = new ColorAdjust();
			adj.setBrightness(-.85);
			h.setEffect(adj);
			h.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				if (inControl != h.getH()) {
					inControl = h.getH();
					adj.setBrightness(0);
					h.setEffect(adj);
					b.setBrightness(0);
					k.setBrightness(0);
				} else {
					inControl = null;
					b.setBrightness(-.6);
					k.setBrightness(-.6);
				}
			});

			h.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
				data.setText(h.getH().toString());
				adj.setBrightness(0);
				h.setEffect(adj);
			});
			h.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
				data.setText((inControl == null) ? null : inControl.toString());
				if (inControl != null && !inControl.equals(h.getH())) {
					adj.setBrightness(-.85);
				} else if (inControl == null)
					adj.setBrightness(-.85);
				h.setEffect(adj);
			});
		}

		lowerSection.getChildren().add(back);
		lowerSection.getChildren().add(data);
		lowerSection.getChildren().add(goToGame);
		lowerSection.setAlignment(Pos.CENTER);
		root.getChildren().add(heros1);
		root.getChildren().add(heros2);
		root.getChildren().add(lowerSection);
		root.setAlignment(Pos.CENTER);
		root.setBackground(new Background(new BackgroundFill(
				new ImagePattern(new Image("HeroChoiceMenuBackground.jpg")), new CornerRadii(0), new Insets(0))));
		return new Scene(root);
	}

	public static Scene spawnMainGame(Stage stage) {
		// the Whole scene here
		double x = Screen.getPrimary().getBounds().getWidth();
		double y = Screen.getPrimary().getBounds().getHeight();
		Label info = new Label();
		info.setTextFill(Color.WHITE);
		info.prefWidth(x / 5);
		info.setFont(Font.font("Imperial", FontWeight.LIGHT, 22));
		info.setText(inControl.display());
		info.setBackground(new Background(new BackgroundFill(Color.DARKRED, new CornerRadii(15), null)));
		info.setPadding(new Insets(10));
		//
		GridPane grid = new GridPane();
		grid.setPrefHeight(y);
		grid.setPrefWidth(x * .8);
		grid.setAlignment(Pos.CENTER_LEFT);
		grid.setMinSize(x * .8, y);
		grid.setMaxSize(x * .8, y);
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				Button button = new Button();
				button.setOnMouseEntered(new EventHandler<Event>() {
					public void handle(Event event) {
						button.setStyle("-fx-border-color: white; -fx-background-color: grey;");
					}
				});
				button.setOnMouseExited(new EventHandler<Event>() {
					public void handle(Event event) {
						button.setStyle("-fx-border-color: white; -fx-background-color: black;");
					}
				});
				button.setStyle("-fx-border-color: white; -fx-background-color:#000000;");
				button.setAlignment(Pos.CENTER);
				button.setPrefSize(grid.getPrefWidth() / 15, grid.getPrefHeight() / 15);
				grid.add(button, j, 14 - i);
				btns[j][14 - i] = button;
			}
		}
		Font font = Font.font("Tahoma", FontWeight.BOLD, 28);
		HBox root = new HBox();
		root.setCache(true);
		root.setSpacing(25);
		root.setPrefSize(x, y);
		VBox contSection = new VBox();
		contSection.setPrefSize(x * .2, y);
		contSection.setAlignment(Pos.CENTER);
		VBox controls = new VBox();
		controls.setPrefWidth(contSection.getPrefWidth());
		controls.setPrefHeight(contSection.getPrefHeight() / 2);
		controls.setSpacing(17);
		controls.setPadding(new Insets(20));
		// THE ATTACK BUTTON
		attack.setStyle("-fx-background-color: Red;-fx-background-radius: 15px;");
		attack.setPrefSize(controls.getPrefWidth() / 1.6, controls.getPrefHeight() / 6.5);
		attack.setTextFill(Color.ANTIQUEWHITE);
		attack.setFont(font);
		attack.setOnMouseClicked(event -> {
			Popup pop = new Popup();
			Label l = new Label();
			l.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(60), null)));
			l.setTextFill(Color.WHITE);
			l.setPrefHeight(y / 6);
			l.setFont(font);
			l.setCenterShape(true);
			pop.getContent().add(l);
			pop.setAutoHide(true);
			pop.show(stage);
			pop.centerOnScreen();
			try {
				inControl.attack();
				if (!Game.checkGameOver() && !Game.checkWin()) {
					l.setText("       Succesful Attack        ");
					l.setCenterShape(true);
					pop.show(stage);
					pop.centerOnScreen();
					updateMap(grid, info);
					inControl.setTarget(null);
					info.setText(inControl.display());
					state(stage);
				}
			} catch (NotEnoughActionsException | InvalidTargetException e1) {
				l.setText("       " + e1.getMessage() + "        ");
				pop.show(stage);
				pop.centerOnScreen();
			}
		});

		// THE CURE BUTTON
		cure.setStyle("-fx-background-color: LimeGreen;-fx-background-radius: 15px;");
		cure.setPrefSize(controls.getPrefWidth() / 1.6, controls.getPrefHeight() / 6.5);
		cure.setTextFill(Color.ANTIQUEWHITE);
		cure.autosize();
		cure.setFont(font);
		cure.setOnMouseClicked(event -> {
			Popup pop = new Popup();
			Label l = new Label();
			state(stage);
			l.setText("        " + "Click On A Zombie Then Click Cure" + "        ");
			l.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(60), null)));
			l.setTextFill(Color.WHITE);
			l.setPrefHeight(y / 6);
			l.setFont(font);
			pop.getContent().add(l);
			pop.setAutoHide(true);
			pop.show(stage);
			pop.centerOnScreen();

			try {
				inControl.cure();
				if (!Game.checkGameOver() && !Game.checkWin()) {
					l.setText("       Succesful Cure        ");
					pop.show(stage);
					pop.centerOnScreen();
					updateMap(grid, info);
					inControl.setTarget(null);
					info.setText(inControl.display());
					SoundEffect healed;
					try {
						healed = new SoundEffect(
								"C:\\Users\\omarg\\eclipse-workspace\\Team7.zip_expanded\\Team7\\src\\Effect\\Healed.mp3");
						healed.soundEffect.play();
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				state(stage);
			} catch (NotEnoughActionsException | InvalidTargetException | NoAvailableResourcesException e1) {
				if (!Game.checkGameOver() && !Game.checkWin()) {
					l.setText("       " + e1.getMessage() + "       ");
					l.setCenterShape(true);
					pop.show(stage);
					pop.centerOnScreen();
				}
			}
		});
		// THE USESPECIAL BUTTON
		useSpecial.setStyle("-fx-background-color: DodgerBlue;-fx-background-radius: 15px;");
		useSpecial.setPrefSize(controls.getPrefWidth() / 1.6, controls.getPrefHeight() / 6.5);
		useSpecial.setTextFill(Color.ANTIQUEWHITE);
		useSpecial.setFont(font);
		Popup pop = new Popup();
		Label l = new Label();
		l.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(60), null)));
		pop.getContent().add(l);
		l.setTextFill(Color.WHITE);
		l.setPrefHeight(y / 6);
		l.setFont(font);
		l.setCenterShape(true);
		pop.setAutoHide(true);
		pop.show(stage);
		pop.centerOnScreen();
		useSpecial.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			updateMap(grid, info);
			try {
				inControl.useSpecial();
				l.setText("      Special Is On      ");
				l.centerShapeProperty();
				pop.centerOnScreen();
				pop.show(stage);
				state(stage);
				inControl.setTarget(null);
				info.setText(inControl.display());
			} catch (Exception e2) {
				l.setText("        " + e2.getMessage() + "        ");
				l.centerShapeProperty();
				pop.centerOnScreen();
				pop.show(stage);
			}
			updateMap(grid, info);
		});
		// END TURN
		Button endTurn = new Button("END TURN");
		endTurn.setStyle("-fx-background-color: DimGray;-fx-background-radius: 15px;");
		endTurn.setPrefSize(controls.getPrefWidth() / 1.6, controls.getPrefHeight() / 6.5);
		endTurn.setTextFill(Color.ANTIQUEWHITE);
		endTurn.setFont(font);
		endTurn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			updateMap(grid, info);
			try {
				Game.endTurn();
				if (!Game.checkGameOver() && !Game.checkWin()) {
					l.setText("     TURN ENDED     ");
					l.centerShapeProperty();
					pop.show(stage);
					pop.centerOnScreen();
				}
				info.setText(inControl.display());
				state(stage);
			} catch (NotEnoughActionsException | InvalidTargetException e3) {
				l.setText("      " + e3.getMessage() + "       ");
				l.centerShapeProperty();
				pop.show(stage);
				pop.centerOnScreen();
			}
			updateMap(grid, info);
		});
		// THE MOVE UP BUTTON
		ImageView up = new ImageView("up.png");
		up.fitWidthProperty().bind(controls.widthProperty().divide(4));
		up.fitHeightProperty().bind(controls.heightProperty().divide(6.5));
		up.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			try {
				Point loc = inControl.getLocation();
				if (inBounds(new Point(loc.x + 1, loc.y)) && Game.map[loc.x + 1][loc.y] instanceof TrapCell) {
					l.setText("      YOU ENTERED A TRAP.      ");
					l.centerShapeProperty();
					pop.show(stage);
					pop.centerOnScreen();
				}
				inControl.move(Direction.UP);
				updateMap(grid, info);
				state(stage);
			} catch (MovementException | NotEnoughActionsException e1) {
				l.setText("        " + e1.getMessage() + "        ");
				l.centerShapeProperty();
				pop.show(stage);
				pop.centerOnScreen();
			}
		});
		// THE REST OF THE MOVE BUTTONS
		HBox lowBar = new HBox();
		lowBar.setPrefSize(controls.getPrefWidth(), controls.getPrefHeight() / 6.5);
		lowBar.setSpacing(15);
		ImageView down = new ImageView("up.png");
		down.setRotate(180);
		down.fitWidthProperty().bind(controls.widthProperty().divide(4));
		down.fitHeightProperty().bind(controls.heightProperty().divide(6.5));
		down.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			try {
				Point loc = inControl.getLocation();
				if (inBounds(new Point(loc.x - 1, loc.y)) && Game.map[loc.x - 1][loc.y] instanceof TrapCell) {
					l.setText("      YOU ENTERED A TRAP.      ");
					l.centerShapeProperty();
					pop.show(stage);
					pop.centerOnScreen();
				}
				inControl.move(Direction.DOWN);
				updateMap(grid, info);
				state(stage);
			} catch (Exception e1) {
				l.setText("        " + e1.getMessage() + "        ");
				l.centerShapeProperty();
				pop.show(stage);
				pop.centerOnScreen();
			}
		});
		ImageView right = new ImageView("up.png");
		right.fitWidthProperty().bind(controls.widthProperty().divide(4));
		right.fitHeightProperty().bind(controls.heightProperty().divide(6.5));
		right.setRotate(90);
		right.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			try {
				Point loc = inControl.getLocation();
				if (inBounds(new Point(loc.x, loc.y + 1)) && Game.map[loc.x][loc.y + 1] instanceof TrapCell) {
					l.setText("      YOU ENTERED A TRAP.      ");
					l.centerShapeProperty();
					pop.show(stage);
					pop.centerOnScreen();
				}
				inControl.move(Direction.RIGHT);
				state(stage);
				updateMap(grid, info);
			} catch (Exception e1) {
				l.setText("        " + e1.getMessage() + "        ");
				l.centerShapeProperty();
				pop.show(stage);
				pop.centerOnScreen();
			}
		});
		ImageView left = new ImageView("up.png");
		left.fitWidthProperty().bind(controls.widthProperty().divide(4));
		left.fitHeightProperty().bind(controls.heightProperty().divide(6.5));
		left.setRotate(270);
		left.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			try {
				Point loc = inControl.getLocation();
				if (inBounds(new Point(loc.x, loc.y - 1)) && Game.map[loc.x][loc.y - 1] instanceof TrapCell) {
					l.setText("      YOU ENTERED A TRAP.      ");
					l.centerShapeProperty();
					pop.show(stage);
					pop.centerOnScreen();
				}
				inControl.move(Direction.LEFT);
				updateMap(grid, info);
				state(stage);
			} catch (Exception e1) {
				l.setText("        " + e1.getMessage() + "        ");
				l.centerShapeProperty();
				pop.show(stage);
				pop.centerOnScreen();
			}
		});
		lowBar.getChildren().addAll(left, down, right);
		// lowBar.setAlignment(Pos.CENTER);
		// CREATE A VBOX TO HOLD THE MOVEMENT ARROWS
		VBox moves = new VBox();
		moves.setPrefSize(contSection.getPrefWidth(), contSection.getPrefHeight() / 4);
		moves.setSpacing(10);
		moves.getChildren().addAll(up, lowBar);
		moves.setAlignment(Pos.BOTTOM_CENTER);
		// ADDING THE BUTTONS TO THE VBOXES AND THEN ADDING THE VBOXES TO THE PANE
		controls.getChildren().addAll(attack, cure, useSpecial, endTurn, moves);
		controls.setAlignment(Pos.CENTER);
		// CHANGE BELOW
		contSection.getChildren().addAll(info, controls);
		// CHANGE ABOVE
		root.getChildren().addAll(grid, contSection);
		updateMap(grid, info);
		root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		Popup instr = new Popup();
		Label tm = new Label(
				"      Click A Character To Set It As A Target      \n          Double Click A Hero To Control It          ");
		tm.setFont(Font.font("Imperial", FontWeight.EXTRA_BOLD, 35));
		tm.setTextFill(Color.WHITE);
		tm.setPadding(new Insets(10));
		tm.setBackground(new Background(new BackgroundFill(Color.DARKRED, new CornerRadii(25), null)));
		instr.getContent().add(tm);
		instr.show(stage);
		instr.setAutoHide(true);
		Timeline t = new Timeline();
		t.setDelay(Duration.seconds(2));
		t.play();
		t.setOnFinished(e -> instr.hide());

		instr.centerOnScreen();
		return new Scene(root);

	}

	public static void updateMap(GridPane grid, Label info) {
		double x = Screen.getPrimary().getBounds().getWidth();
		double y = Screen.getPrimary().getBounds().getHeight();
		info.setText(inControl.display());
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				ImageView v = new ImageView();
				Button button = btns[j][14 - i];
				button.setPrefSize(grid.getPrefWidth() / 15, grid.getPrefHeight() / 15);
				if (Game.map[i][j].isVisible()) {
					grid.getChildren().remove(button);
					if (Game.map[i][j] instanceof CharacterCell) {
						CharacterCell c = (CharacterCell) Game.map[i][j];
						if (c.getCharacter() instanceof Hero) {
							v = new ImageView(c.getCharacter().getName() + ".png");
							/*
							 * for(int k = 0; k < btns.length; k++) { for(int l = 0; l < btns[k].length;
							 * l++) { if(btns[k][l].getGraphic().equals(v)) { TranslateTransition translate
							 * = new TranslateTransition(); if() } } }
							 */
							button.setOnMouseClicked(e -> {
								if (e.getClickCount() == 2) {
									inControl = (Hero) c.getCharacter();
								} else {
									inControl.setTarget(c.getCharacter());
								}
								info.setText(inControl.display());
							});
							button.setOnMouseEntered(e -> {
								info.setText(((Hero) c.getCharacter()).display());
							});
							button.setOnMouseExited(e -> {
								info.setText(inControl.display());
							});
						} else if (c.getCharacter() instanceof Zombie) {
							v = new ImageView("zombie.png");
							button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
								inControl.setTarget(c.getCharacter());
								info.setText(inControl.display());
							});
						} else if (c.getCharacter() == null) {
							button = btns[j][14 - i];
							grid.getChildren().remove(button);
							button.setOnMouseClicked(null);
							button.setOnMouseEntered(null);
							button.setOnMouseExited(null);
							info.setText(inControl.display());
						}
					} else if (Game.map[i][j] instanceof CollectibleCell) {
						CollectibleCell c = (CollectibleCell) Game.map[i][j];
						if (c.getCollectible() instanceof Supply) {
							v = new ImageView("supply.png");
						} else
							v = new ImageView("vaccine.png");
					}
				} else {
					v = new ImageView(invis.getImage());
					button = btns[j][14 - i];
					grid.getChildren().remove(button);
					button.setOnMouseClicked(null);
				}
				v.fitWidthProperty().bind(button.prefWidthProperty().divide(1.35));
				v.fitHeightProperty().bind(button.prefHeightProperty().divide(1.35));
				button.setStyle("-fx-border-color: white; -fx-background-color: Black;");
				// button.setAlignment(Pos.CENTER);
				button.setGraphic(v);
				btns[j][14 - i] = button;
				grid.getChildren().add(button);
			}
		}
	}

	public static void state(Stage stage) {
		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		Label txt = new Label();
		txt.setFont(Font.font("Imperial", FontWeight.EXTRA_BOLD, 100));
		if (Game.checkGameOver()) {
			try {
				SoundEffect se = new SoundEffect(
						"C:\\Users\\omarg\\eclipse-workspace\\Team7.zip_expanded\\Team7\\src\\Effect\\YouLost.mp3");
				se.soundEffect.play();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			txt.setText("YOU LOST");
			txt.setTextFill(Color.RED);
		}
		if (Game.checkWin()) {
			txt.setText("YOU WON");
			txt.setTextFill(Color.LIMEGREEN);
		}
		ImageView v = new ImageView("exitButton.png");
		v.fitWidthProperty().bind(root.widthProperty().divide(8));
		v.fitHeightProperty().bind(root.heightProperty().divide(10));
		root.setSpacing(200);
		v.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Platform.exit());
		root.getChildren().addAll(txt, v);
		root.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image("gridBack.jpg")), null, null)));
		Scene end = new Scene(root);
		if (Game.checkGameOver() || Game.checkWin()) {
			FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), end.getRoot());
			fadeIn.setFromValue(0.0);
			fadeIn.setToValue(1.0);
			//
			FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), root);
			fadeOut.setFromValue(1.0);
			fadeOut.setToValue(0.0);
			fadeOut.setOnFinished(event -> {
				stage.setScene(end);
				stage.setFullScreen(true);
				fadeIn.play();
			});
			fadeOut.play();
		}
	}

	public static boolean inBounds(Point x) {
		if (x.x > 14 || x.x < 0 || x.y > 14 || x.y < 0)
			return false;
		return true;
	}

	public static void main(String[] args) {
		Game game = new Game();
		try {
			Game.loadHeroes("C:\\Users\\omarg\\eclipse-workspace\\Team7.zip_expanded\\Team7\\Heroes.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		launch(args);
	}

}
