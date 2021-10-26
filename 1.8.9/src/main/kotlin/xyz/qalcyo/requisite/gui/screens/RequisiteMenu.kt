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

package xyz.qalcyo.requisite.gui.screens

import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.universal.GuiScale
import net.minecraft.client.Minecraft
import xyz.qalcyo.requisite.Requisite
import xyz.qalcyo.requisite.core.gui.main.RequisiteMenuPage
import xyz.qalcyo.requisite.core.gui.main.impl.RequisiteControlsPage
import xyz.qalcyo.requisite.core.gui.main.impl.RequisiteMainPage
import xyz.qalcyo.requisite.core.gui.main.impl.RequisiteNetworkingPage
import xyz.qalcyo.requisite.core.integration.mods.IMod
import xyz.qalcyo.requisite.core.integration.mods.IModConfigurationMenu
import java.awt.Color
import java.util.concurrent.CompletableFuture

class RequisiteMenu : WindowScreen(
    restoreCurrentGuiOnClose = true,
    newGuiScale = GuiScale.scaleForScreenSize().ordinal
), IModConfigurationMenu {

    private val pages = arrayOf(
        RequisiteMainPage(),
        RequisiteControlsPage(),
        RequisiteNetworkingPage()
    )
    private var page = pages[0]
    private val text = UIText(page.title)

    private val content = UIContainer()

    init {
        val pageListContainer = UIContainer().constrain {
            width = ChildBasedSizeConstraint() / 2
            height = RelativeConstraint()
        } childOf window
        for (page in pages) {
            val pageButton = UIImage(CompletableFuture.supplyAsync { page.icon }).constrain {
                x = CenterConstraint()
                y = SiblingConstraint(1f)
                width = 32.pixels()
                height = 32.pixels()
            }.onMouseClick {
                initializePage(page)
            } childOf pageListContainer
        }

        val pageListDivider = UIBlock(Color.BLACK).constrain {
            x = SiblingConstraint()
            width = 2.pixels()
            height = RelativeConstraint()
        } childOf window

        content.constrain {
            x = SiblingConstraint()
            width = RelativeConstraint()
            height = RelativeConstraint()
        } childOf window
    }

    private fun initializePage(page: RequisiteMenuPage) {
        this@RequisiteMenu.page = page
        println(page)

        content.clearChildren()
        page.constrain {
            width = RelativeConstraint()
            height = RelativeConstraint()
        } childOf content
    }

    override fun setWorldAndResolution(mc: Minecraft?, width: Int, height: Int) {
        newGuiScale = GuiScale.scaleForScreenSize().ordinal
        super.setWorldAndResolution(mc, width, height)
    }

    override fun open() = Requisite.getInstance().guiHelper.open(this)
    override fun getMod(): IMod = Requisite.getInstance()

}
