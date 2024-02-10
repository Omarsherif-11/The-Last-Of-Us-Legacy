package views;

import java.io.File;
import java.net.MalformedURLException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffect {
	Media media;
	public MediaPlayer soundEffect;
	public SoundEffect(String filePath) throws MalformedURLException {
		media = new Media(new File(filePath).toURI().toURL().toString());
		soundEffect = new MediaPlayer(media);
		soundEffect.setCycleCount(1);
	}
}
