/*
 * Copyright (c) 2018. Projekt Development LLC - All Rights Reserved.
 * This file is proprietary and confidential, part of Substratum Lite.
 *
 * Substratum Lite is not free software: You may not redistribute it and/or modify it without
 * expressed permission from the above corporation, or legal action will be taken against the
 * offender.
 *
 * Unauthorized copying of this file via any medium is strictly prohibited.
 */

package projekt.andromeda.client

import projekt.andromeda.client.CommandInternal.COMMAND
import projekt.andromeda.client.CommandInternal.SCREEN_CAPTURE
import projekt.andromeda.client.CommandInternal.run

/**
 * The AndromedaScreenCapture class provides utility to capture the device screen.
 */
object AndromedaScreenCapture {

    private const val CAPTURE = "capture"

    /**
     * Captures screen to the specified path.
     * @param path Path to save the PNG output file.
     */
    fun capture(path: String) {
        run(COMMAND, SCREEN_CAPTURE, CAPTURE, listOf(path))
    }
}