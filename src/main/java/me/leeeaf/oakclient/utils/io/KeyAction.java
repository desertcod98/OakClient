package me.leeeaf.oakclient.utils.io;

import org.lwjgl.glfw.GLFW;

public enum KeyAction {
        PRESS,
        RELEASE,
        REPEAT;

        public static KeyAction getKeyAction(int action){
            if(action == GLFW.GLFW_PRESS) return PRESS;
            if(action == GLFW.GLFW_RELEASE) return RELEASE;
            return REPEAT;
        }
}
