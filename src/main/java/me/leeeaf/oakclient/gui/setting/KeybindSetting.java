package me.leeeaf.oakclient.gui.setting;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IKeybindSetting;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableTextContent;
import org.lwjgl.glfw.GLFW;

public class KeybindSetting extends Setting<Integer> implements IKeybindSetting {
	public KeybindSetting (String displayName, String configName, String description, IBoolean visible, Integer value) {
		super(displayName,configName,description,visible,value);
	}

	@Override
	public int getKey() {
		if(getValue()!=null){
			return getValue();
		}else{
			return -1;
		}
	}

	@Override
	public void setKey (int key) {
		if(key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE){
			setValue(null);
		}else{
			setValue(key);
		}
	}

	@Override
	public String getKeyName() {
		//TODO test if this ugly fix doesn't destroy stuff
		String translationKey=InputUtil.Type.KEYSYM.createFromCode(getKey()).getTranslationKey();
		return translationKey.substring(translationKey.lastIndexOf(".") + 1);

//		String translation=new TranslatableTextContent(translationKey).toString();
//		if (!translation.equals(translationKey)) return translation;
//		return InputUtil.Type.KEYSYM.createFromCode(getKey()).getLocalizedText().getString();
	}
}
