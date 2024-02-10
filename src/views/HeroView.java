package views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.characters.*;

public class HeroView extends ImageView{
	private Hero h;
    public HeroView (String string) {
     super(string);	
    }
    public HeroView (Image i) {
        super(i);	
       }
	public Hero getH() {
		return h;
	}
	public void setH(Hero h) {
		this.h = h;
	}
}
