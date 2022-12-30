package me.medisant.sbpreise.client;

import me.medisant.sbpreise.gui.GuiScreen;
import me.medisant.sbpreise.gui.MainGui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class SbpreiseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerKeyBindings();
    }

    private void registerKeyBindings() {
        KeyBinding openGuiKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.sbpreise.open_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J, "Sbpreise"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKeybinding.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new GuiScreen(new MainGui()));
            }
        });
    }
}
