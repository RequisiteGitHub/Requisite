package xyz.deftu.requisite.cosmetics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import xyz.deftu.requisite.core.cosmetics.CosmeticData;
import xyz.deftu.requisite.core.cosmetics.ICosmetic;
import xyz.deftu.requisite.core.cosmetics.ICosmeticInitializer;
import xyz.deftu.requisite.cosmetics.impl.CloakCosmetic;

import java.util.List;

public class CosmeticInitializer implements ICosmeticInitializer<AbstractClientPlayer> {

    private final CosmeticRenderer renderer;

    public CosmeticInitializer() {
        this.renderer = new CosmeticRenderer();
    }

    public void initialize(List<ICosmetic<AbstractClientPlayer>> cosmetics, CosmeticData data) {
        cosmetics.add(new CloakCosmetic(data));
    }

    public void finish(List<ICosmetic<AbstractClientPlayer>> cosmetics) {
        renderer.initialize(cosmetics);

        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("default").addLayer(renderer);
        Minecraft.getMinecraft().getRenderManager().getSkinMap().get("slim").addLayer(renderer);
    }

    public CosmeticRenderer renderer() {
        return renderer;
    }

}