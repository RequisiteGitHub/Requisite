/*
 * Requisite - Minecraft library mod
 *  Copyright (C) 2021 Qalcyo
 *
 * Requisite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * Requisite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Requisite. If not, see <https://www.gnu.org/licenses/>.
 */

package xyz.qalcyo.requisite.core;

import org.apache.logging.log4j.Logger;
import xyz.qalcyo.mango.Strings;
import xyz.qalcyo.requisite.core.files.ConfigurationManager;
import xyz.qalcyo.requisite.core.integration.mods.IMod;
import xyz.qalcyo.requisite.core.integration.mods.IModConfigurationMenu;
import xyz.qalcyo.requisite.core.keybinds.KeyBindRegistry;
import xyz.qalcyo.requisite.core.integration.mods.ModMetadata;
import xyz.qalcyo.requisite.core.util.*;
import xyz.qalcyo.requisite.core.util.messages.IMessageQueue;
import xyz.qalcyo.requisite.core.commands.CommandRegistry;
import xyz.qalcyo.requisite.core.files.FileManager;
import xyz.qalcyo.requisite.core.files.configs.PrivacyConfigurations;
import xyz.qalcyo.requisite.core.integration.hypixel.HypixelHelper;
import xyz.qalcyo.requisite.core.integration.mods.IModIntegration;
import xyz.qalcyo.requisite.core.networking.RequisiteClientSocket;
import xyz.qalcyo.requisite.core.notifications.INotifications;
import xyz.qalcyo.requisite.core.rendering.IEnhancedFontRenderer;
import xyz.qalcyo.json.entities.JsonObject;
import xyz.qalcyo.json.util.JsonApiHelper;
import xyz.qalcyo.requisite.gui.components.factory.IComponentFactory;
import xyz.qalcyo.simpleeventbus.SimpleEventBus;

import java.io.File;
import java.net.URI;
import java.util.Base64;

public interface RequisiteAPI extends IMod {

    /**
     * Initializes all of Requisite's core features for the version requested.
     *
     * @param gameDir The game directory of this version.
     * @return Whether initialization was completed successfully or not.
     */
    boolean initialize(File gameDir);
    /**
     * Initializes Requisite, then completes it's own actions.
     *
     * @param gameDir The game directory of this version.
     * @return Whether initialization was completed successfully or not.
     */
    default boolean finish(File gameDir) {
        boolean initial = initialize(gameDir);
        getConfigurationManager().addConfigurable(getPrivacyConfigurations());
        getModIntegration().registerIntegratedMod(this);
        return initial;
    }

    /**
     * Provides an instance of Requisite's Log4J logger.
     *
     * @return Requisite's logger.
     */
    default Logger getLogger() {
        return RequisiteDefaultImplementations.LOGGER;
    }
    /**
     * Provides an instance of Requisite's JVM argument parser.
     *
     * @return Requisite's JVM argument parser.
     */
    default RequisiteJavaArguments getJavaArguments() {
        return RequisiteDefaultImplementations.JAVA_ARGUMENTS;
    }
    /**
     * Provides an instance of Requisite's {@link SimpleEventBus}.
     *
     * @return Requisite's event bus.
     */
    default SimpleEventBus getEventBus() {
        return RequisiteDefaultImplementations.EVENT_BUS;
    }

    /**
     * Provides an instance of Requisite's file and directory manager.
     *
     * @return Requisite's file and directory manager.
     */
    FileManager getFileManager();
    /**
     * Provides an instance of Requisite's internal configuration manager.
     *
     * @return Requisite's internal configuration manager.
     */
    ConfigurationManager getConfigurationManager();
    /**
     * Provides an instance of Requisite's notifications service.
     *
     * @return Requisite's notifications service.
     */
    INotifications getNotifications();
    /**
     * Provides an instance of Requisite's internal websocket.
     *
     * @return Requisite's internal websocket.
     */
    RequisiteClientSocket getRequisiteSocket();
    /**
     * Provides an instance of Requisite's mod integration API.
     *
     * @return Requisite's mod integration API.
     */
    IModIntegration getModIntegration();
    /**
     * Provides an instance of Requisite's custom command registry.
     *
     * @return Requisite's command registry.
     */
    CommandRegistry getCommandRegistry();
    /**
     * Provides an instance for Requisite's KeyBind API.
     *
     * @return Requisite's KeyBind API.
     */
    default KeyBindRegistry getKeyBindRegistry() {
        return RequisiteDefaultImplementations.KEY_BIND_REGISTRY;
    }
    /**
     * Provides an instance of Requisite's custom GUI component factory.
     *
     * @return Requisite's custom GUI component factory.
     */
    IComponentFactory getComponentFactory();
    /**
     * Provides an instance of Requisite's internal event manager.
     *
     * @return Requisite's internal event manager.
     */
    RequisiteEventManager getInternalEventManager();
    /**
     * Provides an instance of Requisite's internal event listener.
     *
     * @return Requisite's internal event listener.
     */
    IEventListener getInternalEventListener();

    /**
     * Provides an instance of Requisite's privacy configurations.
     *
     * @return Requisite's privacy configurations.
     */
    default PrivacyConfigurations getPrivacyConfigurations() {
        return RequisiteDefaultImplementations.PRIVACY_CONFIGURATIONS;
    }

    /**
     * Opens Requisite's main menu, providing access to most Requisite configurations and services.
     */
    void openMenu();
    /**
     * Creates an instance of Requisite's main menu.
     *
     * @return Requisite's main menu.
     */
    IModConfigurationMenu createMainMenu();

    /**
     * Provides an instance of Requisite's enhanced font renderer utility, which gives you more options for rendering text to the screen.
     *
     * @return Requisite's enhanced font renderer utility.
     */
    IEnhancedFontRenderer getEnhancedFontRenderer();
    /**
     * Provides an instance of Requisite's GUI utility, allowing you to open GUIs easily.
     *
     * @return Requisite's GUI utility.
     */
    IGuiHelper<?> getGuiHelper();
    /**
     * Provides an instance of Requisite's player utility, giving you access to some of the player's data.
     *
     * @return Requisite's player utility.
     */
    IPlayerHelper getPlayerHelper();
    /**
     * Provides an instance of Requisite's chat utility, giving you easy access to the in-game chat.
     *
     * @return Requisite's chat utility.
     */
    IChatHelper getChatHelper();
    /**
     * Provides an instance of Requisite's colour utility, adding more colour to your mod.
     *
     * @return Requisite's colour utility.
     */
    default ColourHelper getColourHelper() {
        return RequisiteDefaultImplementations.COLOUR_HELPER;
    }
    /**
     * Provides an instance of Requisite's logging utility, debugging!
     *
     * @return Requisite's logging utility.
     */
    default LoggingHelper getLoggingHelper() {
        return RequisiteDefaultImplementations.LOGGING_HELPER;
    }
    /**
     * Provides an instance of Requisite's universal logger, allowing you to create a logger specifically for the class it's used in.
     *
     * @return Requisite's universal logger.
     */
    default UniversalLogger getUniversalLogger() {
        return RequisiteDefaultImplementations.UNIVERSAL_LOGGER;
    }
    /**
     * Provides an instance of Requisite's clipboard utility, giving you access to the contents of the user's clipboard.
     *
     * @return Requisite's clipboard utility.
     */
    default ClipboardHelper getClipboardHelper() {
        return RequisiteDefaultImplementations.CLIPBOARD_HELPER;
    }
    /**
     * Provides an instance of Requisite's date utility, letting you check for special events.
     *
     * @return Requisite's date helper.
     */
    default DateHelper getDateHelper() {
        return RequisiteDefaultImplementations.DATE_HELPER;
    }
    /**
     * Provides an instance of Requisite's easing utility.
     *
     * @return Requisite's easing utility.
     */
    default EasingHelper getEasingHelper() {
        return RequisiteDefaultImplementations.EASING_HELPER;
    }
    /**
     * Provides an instance of Requisite's math utility, making your calculations easier.
     *
     * @return Requisite's math utility.
     */
    default MathHelper getMathHelper() {
        return RequisiteDefaultImplementations.MATH_HELPER;
    }
    /**
     * Provides an instance of Requisite's mouse utility, allowing you to interact with the mouse easily.
     *
     * @return Requisite's mouse utility.
     */
    IMouseHelper getMouseHelper();
    /**
     * Provides an instance of Requisite's reflection utility. This makes it easier to interact with Java's reflection API.
     *
     * @return Requisite's reflection utility.
     */
    default ReflectionHelper getReflectionHelper() {
        return RequisiteDefaultImplementations.REFLECTION_HELPER;
    }
    /**
     * Provides an instance of Requisite's position helper.
     *
     * @return Requisite's position helper.
     */
    IPositionHelper getPositionHelper();
    /**
     * Provides an instance of Requisite's roman numeral cache.
     *
     * @return Requisite's roman numeral cache.
     */
    default RomanNumeral getRomanNumerals() {
        return RequisiteDefaultImplementations.ROMAN_NUMERAL;
    }
    /**
     * Provides an instance of Requisite's Hypixel server utility.
     *
     * @return Requisite's Hypixel utility.
     */
    default HypixelHelper getHypixelHelper() {
        return RequisiteDefaultImplementations.HYPIXEL_HELPER;
    }
    /**
     * Provides an instance of Requisite's rendering utility.
     *
     * @return Requisite's rendering helper.
     */
    IRenderHelper getRenderHelper();
    /**
     * Provides an instance of Requisite's string utility.
     *
     * @return Requisite's string utility.
     */
    default StringHelper getStringHelper() {
        return RequisiteDefaultImplementations.STRING_HELPER;
    }
    /**
     * Provides an instance of Requisite's in-game chat message queue. This sends messages as the player.
     *
     * @return Requisite's in-game chat message queue.
     */
    IMessageQueue getMessageQueue();
    /**
     * Provides an instance of Requisite's server utility.
     *
     * @return Requisite's server utility.
     */
    IServerHelper getServerHelper();
    /**
     * Provides an instance of Requisite's Mojang API utility.
     *
     * @return Requisite's Mojang API utility.
     */
    default MojangAPI getMojangApi() {
        return RequisiteDefaultImplementations.MOJANG_API;
    }
    /**
     * Provides an instance of Requisite's OpenGL utility.
     *
     * @return Requisite's OpenGL utility.
     */
    IGlHelper getGlHelper();

    /* Default. */

    /**
     * Fetches and provides Requisite's websocket URI.
     *
     * @return Requisite's websocket URI.
     */
    default URI fetchSocketUri() {
        if (getJavaArguments().isSocketDebug()) {
            return URI.create("ws://localhost:8080/");
        }

        if (getJavaArguments().getSocketUri() != null) {
            return URI.create(getJavaArguments().getSocketUri());
        }

        JsonObject object = JsonApiHelper.getJsonObject(getJavaArguments().getSocketUrl());
        String encoded = object.getAsString("uri");
        for (int i = 0; i < object.getAsInt("loop"); i++) {
            encoded = new String(Base64.getDecoder().decode(encoded));
        }

        return URI.create(encoded);
    }

    /**
     * @param suffix The suffix to add after the Requisite text.
     * @return Requisite's prefix for chat messages.
     */
    default String getChatPrefix(String suffix) {
        return ChatColour.GRAY + "[" + getJavaArguments().getChatPrefixColour() + name() + (Strings.isNullOrEmpty(suffix) ? "" : " " + suffix) + ChatColour.GRAY + "]";
    }

    /**
     * @return Requisite's prefix for chat messages.
     */
    default String getChatPrefix() {
        return getChatPrefix(null);
    }

    /**
     * @return Requisite's name.
     */
    default String name() {
        return RequisiteInfo.NAME;
    }
    /**
     * @return Requisite's current version.
     */
    default String version() {
        return RequisiteInfo.VER;
    }
    /**
     * @return Requisite's ID for mod loader purposes.
     */
    default String id() {
        return RequisiteInfo.ID;
    }

    /**
     * Provides Requisite's mod integration API it's own metadata.
     *
     * @return Requisite's own metadata.
     */
    default ModMetadata getMetadata() {
        return ModMetadata.from(name(), version())
                .setCommand("/requisite");
    }

    /**
     * Retrieves a cached instance of Requisite.
     *
     * @return An instance of Requisite.
     */
    static RequisiteAPI retrieveInstance() {
        try {
            RequisiteAPI cached = RequisiteDefaultImplementations.INSTANCE;
            if (cached == null)
                return RequisiteDefaultImplementations.INSTANCE = (RequisiteAPI) Class.forName("xyz.qalcyo.requisite.Requisite").getDeclaredMethod("getInstance").invoke(null);
            return cached;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}