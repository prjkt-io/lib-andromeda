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
import projekt.andromeda.client.CommandInternal.INPUT
import projekt.andromeda.client.CommandInternal.run
import java.util.ArrayList

/**
 * Input controller to sends key events to the device, either by their keycode, or by
 * desired character input
 */
object AndromedaInput {
    /**
     * This flag is set to send a long press key event
     * @see keyEvent
     */
    const val FLAG_KEY_EVENT_LONGPRESS = 1

    /**
     * To sends key event to the device.
     * @param flags Flags for key event.
     * @param keyCodes Array of key codes to send.
     * @see FLAG_KEY_EVENT_LONGPRESS
     */
    fun keyEvent(flags: Int, keyCodes: Array<Int>) {
        val args = ArrayList<String>()
        args.add(flags.toString())
        for (keycode in keyCodes) {
            args.add(keycode.toString())
        }
        run(COMMAND, INPUT, "key_event", args)
    }

    /**
     * To send text to the device.
     * @param text String text to input.
     */
    fun text(text: String) {
        if (!text.matches("\\s+".toRegex())) {
            val args = ArrayList<String>()
            args.add(text)
            run(COMMAND, INPUT, "text", args)
        }
    }
}
