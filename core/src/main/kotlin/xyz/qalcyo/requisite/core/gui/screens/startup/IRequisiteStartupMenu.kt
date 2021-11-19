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

package xyz.qalcyo.requisite.core.gui.screens.startup

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.Window
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.utils.withAlpha
import xyz.qalcyo.requisite.core.RequisiteAPI
import xyz.qalcyo.requisite.core.RequisitePalette
import xyz.qalcyo.requisite.core.gui.components.Button
import xyz.qalcyo.requisite.core.gui.components.builders.ButtonBuilder
import java.awt.Color

interface IRequisiteStartupMenu {
    
    val window: Window
    
    val slides: Array<RequisiteStartupSlide>
    var slide: RequisiteStartupSlide
    val content: UIContainer

    var slideBox: UIBlock
    val slideBoxes: MutableList<UIBlock>
    
    fun initialize() {
        val background = UIBlock(Color(44, 45, 50).withAlpha(255)).constrain {
            width = RelativeConstraint()
            height = RelativeConstraint()
        } childOf window
        
        val window = UIContainer().constrain {
            x = 30.pixels()
            y = 30.pixels()
            width = FillConstraint() - 20.pixels()
            height = FillConstraint() - 20.pixels()
        } childOf window
        
        val slideBoxesContainer = UIContainer().constrain {
            x = CenterConstraint()
            y = 4.pixels()
            width = ChildBasedSizeConstraint(3f)
            height = ChildBasedSizeConstraint()
        } childOf window

        for (slide in slides) {
            val slideBox = UIBlock(RequisitePalette.getPrimary().asColor()).constrain {
                x = SiblingConstraint(3f)
                y = CenterConstraint()
                width = 31.pixels()
                height = 5.pixels()
            }.onMouseClick {
                initializeSlide(slide)
                initializeSlideBox(this as UIBlock)
            } childOf slideBoxesContainer
            slideBoxes.add(slideBox as UIBlock)
            if (slide == this@IRequisiteStartupMenu.slide) {
                initializeSlideBox(slideBox)
            }
        }

        val slideBoxesDivider = UIBlock(RequisitePalette.getPrimary().asColor()).constrain {
            y = slideBoxesContainer.getBottom().pixels()
            width = RelativeConstraint()
            height = 2.pixels()
        }

        val continueButton = ButtonBuilder({
            progress()
        }, "Continue").build(RequisiteAPI.retrieveInstance().componentFactory).constrain {
            x = CenterConstraint()
            y = 5.pixels(true)
            width = Button.DEFAULT_WIDTH_SMALL_PIXELS
        } childOf window

        for (slide in slides) {
            slide.constrain {
                width = RelativeConstraint()
                height = RelativeConstraint()
            } childOf content
        }

        initializeSlide(slide)

        val inspector = Inspector(window) childOf window
    }
    
    fun initializeSlide(slide: RequisiteStartupSlide) {
        this@IRequisiteStartupMenu.slide = slide
        for (slide in slides) {
            slide.hide(true)
        }

        slide.unhide(true)
        slide.reset()
        slide.initialize()
    }

    fun initializeSlideBox(box: UIBlock) {
        this@IRequisiteStartupMenu.slideBox = box
        for (slideBox in slideBoxes) {
            slideBox.animate {
                setColorAnimation(Animations.OUT_EXP, 1f, ConstantColorConstraint(Color.WHITE))
            }
        }

        box.animate {
            setColorAnimation(Animations.OUT_EXP, 1f, ConstantColorConstraint(RequisitePalette.getPrimary().asColor()))
        }
    }

    fun progress() {
        println(slides.indexOf(slide))
        println(slides.size)
        if (slides.indexOf(slide) == slides.size)
            finish()
        initializeSlide(slides[slides.indexOf(slide) + 1])
        initializeSlideBox(slideBoxes[slideBoxes.indexOf(slideBox) + 1])
    }

    fun finish()
    
}