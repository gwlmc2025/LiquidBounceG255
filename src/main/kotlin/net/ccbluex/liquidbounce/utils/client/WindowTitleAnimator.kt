/*
 * This file is part of LiquidBounce (https://github.com/CCBlueX/LiquidBounce)
 *
 * Copyright (c) 2015 - 2026 CCBlueX
 *
 * LiquidBounce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LiquidBounce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LiquidBounce. If not, see <https://www.gnu.org/licenses/>.
 */
package net.ccbluex.liquidbounce.utils.client

import net.ccbluex.liquidbounce.event.EventListener
import net.ccbluex.liquidbounce.event.events.GameTickEvent
import net.ccbluex.liquidbounce.event.events.WorldChangeEvent
import net.ccbluex.liquidbounce.event.handler
import net.ccbluex.liquidbounce.utils.client.mc
import net.minecraft.SharedConstants

/**
 * Window Title Animator
 *
 * Animates the window title with a scrolling effect.
 * - When loading: "LiquidBounceG255 - Loading | 1.21.11"
 * - Normal: Scrolls "L_" -> "Liq_" -> ... -> "LiquidBounceG255 By-G255_"
 *   -> pause 3s -> delete chars -> pause 0.3s -> repeat
 */
object WindowTitleAnimator : EventListener {

    private const val BASE_TITLE = "LiquidBounceG255 By-G255"
    private const val LOADING_TITLE = "LiquidBounceG255 - Loading"
    private const val VERSION_SUFFIX = " | 1.21.11"

    private var tickCounter = 0
    private var currentTitle = ""

    private const val fullTitle = BASE_TITLE + "_"

    // Animation parameters
    private const val PAUSE_AFTER_FULL_TICKS = 60 * 3  // 3 seconds at 60 ticks/second
    private const val PAUSE_AFTER_EMPTY_TICKS = 60 / 3  // 0.3 seconds at 60 ticks/second
    private const val ADD_CHAR_SPEED = 2  // Add one character every X ticks
    private const val DELETE_CHAR_SPEED = 2  // Delete one character every X ticks

    private enum class AnimationState {
        ADDING_CHARS,
        PAUSING_FULL,
        DELETING_CHARS,
        PAUSING_EMPTY
    }

    private var currentState = AnimationState.ADDING_CHARS
    private var pauseCounter = 0
    private var charIndex = 0

    // Track if we're actually loading a world (vs just being on main menu)
    private var isWorldLoading = false

    init {
        mc.window.setTitle(fullTitle + VERSION_SUFFIX)
    }

    @Suppress("unused")
    private val tickHandler = handler<GameTickEvent> {
        tickCounter++

        if (isLoading()) {
            updateLoadingTitle()
        } else {
            updateAnimatedTitle()
        }
    }

    @Suppress("unused")
    private val worldChangeHandler = handler<WorldChangeEvent> {
        // Reset loading state when world changes
        // If we're leaving a world (event.world is null), we might be going back to menu
        // If we're entering a world, loading is complete
        if (it.world != null) {
            isWorldLoading = false
        } else {
            // Leaving a world, going to menu - not loading
            isWorldLoading = false
        }
    }

    /**
     * Check if the game is in a loading state
     * Only return true when actually loading a world (connecting to server),
     * not when on the main menu
     */
    private fun isLoading(): Boolean {
        // If player exists, we're not loading
        if (mc.player != null) return false

        // If level exists, we're not loading
        if (mc.level != null) return false

        // Check if we have an active network connection
        // If yes, we're likely loading a world/server
        // If no, we're on the main menu
        val networkConnection = mc.connection?.connection
        if (networkConnection != null && networkConnection.isConnected) {
            isWorldLoading = true
            return true
        }

        // On main menu, not loading
        isWorldLoading = false
        return false
    }

    /**
     * Update title during loading
     */
    private fun updateLoadingTitle() {
        val newTitle = LOADING_TITLE + VERSION_SUFFIX
        if (currentTitle != newTitle) {
            currentTitle = newTitle
            mc.window.setTitle(newTitle)
        }
    }

    /**
     * Update title with animation
     */
    private fun updateAnimatedTitle() {
        var newTitle = ""

        when (currentState) {
            AnimationState.ADDING_CHARS -> {
                // Add characters one by one
                if (tickCounter % ADD_CHAR_SPEED == 0) {
                    charIndex++
                    if (charIndex > fullTitle.length) {
                        charIndex = fullTitle.length
                        currentState = AnimationState.PAUSING_FULL
                        pauseCounter = 0
                    }
                }
                newTitle = fullTitle.take(charIndex) + VERSION_SUFFIX
            }

            AnimationState.PAUSING_FULL -> {
                // Pause at full title
                pauseCounter++
                if (pauseCounter >= PAUSE_AFTER_FULL_TICKS) {
                    currentState = AnimationState.DELETING_CHARS
                }
                newTitle = fullTitle + VERSION_SUFFIX
            }

            AnimationState.DELETING_CHARS -> {
                // Delete characters one by one
                if (tickCounter % DELETE_CHAR_SPEED == 0) {
                    charIndex--
                    if (charIndex < 0) {
                        charIndex = 0
                        currentState = AnimationState.PAUSING_EMPTY
                        pauseCounter = 0
                    }
                }
                newTitle = fullTitle.take(charIndex) + VERSION_SUFFIX
            }

            AnimationState.PAUSING_EMPTY -> {
                // Pause at empty title
                pauseCounter++
                if (pauseCounter >= PAUSE_AFTER_EMPTY_TICKS) {
                    currentState = AnimationState.ADDING_CHARS
                    charIndex = 0
                }
                newTitle = VERSION_SUFFIX
            }
        }

        if (currentTitle != newTitle) {
            currentTitle = newTitle
            mc.window.setTitle(newTitle)
        }
    }

    /**
     * Reset the animation state
     */
    fun reset() {
        tickCounter = 0
        currentState = AnimationState.ADDING_CHARS
        pauseCounter = 0
        charIndex = 0
        currentTitle = ""
    }
}


