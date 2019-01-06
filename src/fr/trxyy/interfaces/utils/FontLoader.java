package fr.trxyy.interfaces.utils;

import fr.trxyy.base.Configuration;
import javafx.scene.text.Font;

public class FontLoader
{
    public void loadFont(final String s) {
        Font.loadFont(this.getClass().getResourceAsStream(String.valueOf(Configuration.getResourceLocation()) + s), 14.0);
    }
    
    public void setFont(final String fontName, final float size) {
        Font.font(fontName, (double)size);
    }
    
    public static Font loadFont(final String fullFont, final String fontName, final float size) {
        Font.loadFont(FontLoader.class.getResourceAsStream(String.valueOf(Configuration.getResourceLocation()) + fullFont), 14.0);
        final Font font = Font.font(fontName, (double)size);
        return font;
    }
}