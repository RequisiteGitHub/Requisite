package xyz.deftu.requisite.core.cosmetics;

import java.util.List;

public interface ICosmeticRenderer<T> {
    void initialize(List<ICosmetic<T>> cosmetics);
}