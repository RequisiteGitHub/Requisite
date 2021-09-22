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

package xyz.qalcyo.requisite.core.events;

import xyz.qalcyo.simpleeventbus.Cancellable;
import xyz.qalcyo.simpleeventbus.Event;

@Cancellable
public class KeyInputEvent extends Event {
    public final int keyCode;
    public final boolean down;
    public final boolean repeated;
    public KeyInputEvent(int keyCode, boolean down, boolean repeated) {
        this.keyCode = keyCode;
        this.down = down;
        this.repeated = repeated;
    }
}