package io.github.ultimateboomer.smoothboot.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class SmoothBootModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new Screen(Text.of("")) {
            @Override
            protected void init() {
                ConfigHandler.openConfigFile();
                this.client.openScreen(screen);
            }
        };
    }
}
