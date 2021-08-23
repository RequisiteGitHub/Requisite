package xyz.matthewtgm.requisite.core.hud;

import xyz.matthewtgm.json.entities.JsonObject;
import xyz.matthewtgm.requisite.core.IRequisite;
import xyz.matthewtgm.requisite.core.events.RenderHudEvent;
import xyz.matthewtgm.requisite.core.files.ConfigurationManager;
import xyz.matthewtgm.requisite.core.files.IConfigurable;
import xyz.matthewtgm.simpleeventbus.EventSubscriber;
import xyz.matthewtgm.tgmconfig.Configuration;
import xyz.matthewtgm.tgmconfig.Subconfiguration;
import xyz.matthewtgm.tgmconfig.settings.BaseSetting;

import java.util.ArrayList;
import java.util.List;

public class HudRegistry implements IConfigurable {

    private final IRequisite requisite;

    private final List<HudElement> elements = new ArrayList<>();

    public HudRegistry(IRequisite requisite) {
        this.requisite = requisite;
        requisite.getManager().getEventBus().register(this);
    }

    public void register(HudElement element) {
        if (!elements.contains(element)) {
            elements.add(element);

            load(element);
            save(element);
        }
    }

    public void unregister(String name) {
        elements.stream().filter(theElement -> theElement.getName().equals(name)).findFirst().ifPresent(elements::remove);
    }

    public void unregister(HudElement element) {
        unregister(element.getName());
    }

    public void save(ConfigurationManager configurationManager) {
        for (HudElement element : elements) {
           save(element);
        }
    }

    public void save(HudElement element) {
        Configuration configuration = mainConfig();
        if (!configuration.hasKey("hud"))
            configuration.createSubconfiguration("hud");
        Subconfiguration hudConfiguration = configuration.getSubconfiguration("hud");

        JsonObject elementSettings = new JsonObject();
        for (BaseSetting<?> setting : element.getSettings()) {
            elementSettings.add(setting.jsonKey(), setting.get());
        }
        hudConfiguration.add(element.getName(), elementSettings);
    }

    public void load(ConfigurationManager configurationManager) {
        for (HudElement element : elements) {
            load(element);
        }
    }

    public void load(HudElement element) {
        Configuration configuration = mainConfig();
        if (!configuration.hasKey("hud"))
            configuration.createSubconfiguration("hud");
        Subconfiguration keyBindsConfiguration = configuration.getSubconfiguration("hud");

        JsonObject elementSettings = new JsonObject();
        for (BaseSetting setting : element.getSettings()) {
            if (elementSettings.hasKey(setting.jsonKey())) {
                setting.set(elementSettings.get(setting.jsonKey()).getAsJsonPrimitive().getValue());
            }
        }
    }

    public Configuration mainConfig() {
        return requisite.getManager().getConfigurationManager().getConfiguration();
    }

    @EventSubscriber
    private void onHudRender(RenderHudEvent event) {
        for (HudElement element : elements) {
            if (element.toggleSetting.get()) {
                element.render(element.positionSetting.get(), event.partialTicks);
            }
        }
    }

}