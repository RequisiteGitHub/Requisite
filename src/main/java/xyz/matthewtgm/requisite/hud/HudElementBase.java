package xyz.matthewtgm.requisite.hud;

import lombok.Getter;
import xyz.matthewtgm.requisite.configuration.settings.PositionSetting;
import xyz.matthewtgm.requisite.data.ScreenPosition;
import xyz.matthewtgm.tgmconfig.settings.BaseSetting;
import xyz.matthewtgm.tgmconfig.settings.impl.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

public abstract class HudElementBase {

    @Getter private final String name, id, description;
    @Getter protected final List<BaseSetting<?>> settings;
    @Getter protected final BooleanSetting toggle;
    @Getter protected final PositionSetting position;

    public HudElementBase(String name, String id, String description) {
        this.name = name;
        this.id = id;
        this.description = description;

        this.settings = new ArrayList<>();
        addSetting(toggle = new BooleanSetting("Toggle", true));
        addSetting(position = new PositionSetting("Position", new ScreenPosition(10, 10)));
    }

    public HudElementBase(String name, String id) {
        this(name, id, "");
    }

    public HudElementBase(String name) {
        this(name, name.toLowerCase().replace(' ', '_'));
    }

    public abstract void render(float partialTicks);
    protected final void addSetting(BaseSetting<?> setting) {
        settings.add(setting);
    }

    public final boolean isToggled() {
        if (toggle == null)
            return false;
        Boolean state = toggle.get();
        return state != null && state;
    }

    public final boolean toggle() {
        if (toggle == null)
            throw new NullPointerException("Toggle setting is null.");
        return toggle.toggle();
    }

}