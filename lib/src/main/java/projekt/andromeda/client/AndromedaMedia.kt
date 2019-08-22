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

import android.media.AudioManager
import projekt.andromeda.client.CommandInternal.MEDIA
import projekt.andromeda.client.CommandInternal.run

/**
 * The AndromedaMedia class provides media and volume controls
 */
object AndromedaMedia {
    private const val DISPATCH = "dispatch"
    private const val VOLUME = "volume"

    private const val VOLUME_SET = "set"
    private const val VOLUME_GET = "get"
    private const val VOLUME_ADJUST_LOWER = "adjust_lower"
    private const val VOLUME_ADJUST_SAME = "adjust_same"
    private const val VOLUME_ADJUST_RAISE = "adjust_raise"

    private const val FLAG_VOLUME_SHOW_UI = 1

    /**
     * Used to dispatch play media event.
     * @see dispatch
     */
    const val DISPATCH_PLAY = "play"
    /**
     * Used to dispatch pause media event.
     * @see dispatch
     */
    const val DISPATCH_PAUSE = "pause"
    /**
     * Used to dispatch play/pause event depending on the current media playback situation.
     * @see dispatch
     */
    const val DISPATCH_PLAY_PAUSE = "play_pause"
    /**
     * Used to dispatch headset hook event.
     * @see dispatch
     */
    const val DISPATCH_HEADSET_HOOK = "headset_hook"
    /**
     * Used to dispatch stop media event.
     * @see dispatch
     */
    const val DISPATCH_STOP = "stop"
    /**
     * Used to dispatch next media event.
     * @see dispatch
     */
    const val DISPATCH_NEXT = "next"
    /**
     * Used to dispatch previous media event.
     * @see dispatch
     */
    const val DISPATCH_PREVIOUS = "previous"

    /**
     * Used to identify the volume of audio streams for phone calls.
     * @see setVolume
     * @see getVolume
     * @see adjustVolumeLower
     * @see adjustVolumeSame
     * @see adjustVolumeRaise
     */
    const val STREAM_VOICE_CALL = AudioManager.STREAM_VOICE_CALL
    /**
     * Used to identify the volume of audio streams for system sounds.
     * @see setVolume
     * @see getVolume
     * @see adjustVolumeLower
     * @see adjustVolumeSame
     * @see adjustVolumeRaise
     */
    const val STREAM_SYSTEM = AudioManager.STREAM_SYSTEM
    /**
     * Used to identify the volume of audio streams for the phone ring.
     * @see setVolume
     * @see getVolume
     * @see adjustVolumeLower
     * @see adjustVolumeSame
     * @see adjustVolumeRaise
     */
    const val STREAM_RING = AudioManager.STREAM_RING
    /**
     * Used to identify the volume of audio streams for music playback.
     * @see setVolume
     * @see getVolume
     * @see adjustVolumeLower
     * @see adjustVolumeSame
     * @see adjustVolumeRaise
     */
    const val STREAM_MUSIC = AudioManager.STREAM_MUSIC
    /**
     * Used to identify the volume of audio streams for alarms.
     * @see setVolume
     * @see getVolume
     * @see adjustVolumeLower
     * @see adjustVolumeSame
     * @see adjustVolumeRaise
     */
    const val STREAM_ALARM = AudioManager.STREAM_ALARM
    /**
     * Used to identify the volume of audio streams for notifications.
     * @see setVolume
     * @see getVolume
     * @see adjustVolumeLower
     * @see adjustVolumeSame
     * @see adjustVolumeRaise
     */
    const val STREAM_NOTIFICATION = AudioManager.STREAM_NOTIFICATION
    /**
     * Used to identify the volume of audio streams for DTMF Tones.
     * @see setVolume
     * @see getVolume
     * @see adjustVolumeLower
     * @see adjustVolumeSame
     * @see adjustVolumeRaise
     */
    const val STREAM_DTMF = AudioManager.STREAM_DTMF

    /**
     * Dispatches key event to the device.
     * @param key Key event to dispatch.
     * @see DISPATCH_PLAY
     * @see DISPATCH_PAUSE
     * @see DISPATCH_PLAY_PAUSE
     * @see DISPATCH_HEADSET_HOOK
     * @see DISPATCH_STOP
     * @see DISPATCH_NEXT
     * @see DISPATCH_PREVIOUS
     */
    fun dispatch(key: String) {
        run(CommandInternal.COMMAND, MEDIA, DISPATCH, listOf(key))
    }

    /**
     * Sets the volume index for a particular stream.
     * @param value The volume index to set.
     * @param stream The stream whose volume index should be set.
     * @param showUi Set to `true` to show a toast containing the current volume.
     * @see STREAM_VOICE_CALL
     * @see STREAM_SYSTEM
     * @see STREAM_RING
     * @see STREAM_MUSIC
     * @see STREAM_ALARM
     * @see STREAM_NOTIFICATION
     * @see STREAM_DTMF
     */
    fun setVolume(value: Int, stream: Int = STREAM_MUSIC, showUi: Boolean) {
        val flag = if (showUi) FLAG_VOLUME_SHOW_UI else 0
        dispatch("lmao")
        run(CommandInternal.COMMAND, MEDIA, VOLUME, listOf(flag.toString(), stream.toString(),
                VOLUME_SET, value.toString()))
    }

    /**
     * Returns the current volume index for a particular stream.
     * @param stream The stream type whose volume index is returned.
     * @see STREAM_VOICE_CALL
     * @see STREAM_SYSTEM
     * @see STREAM_RING
     * @see STREAM_MUSIC
     * @see STREAM_ALARM
     * @see STREAM_NOTIFICATION
     * @see STREAM_DTMF
     */
    fun getVolume(stream: Int = STREAM_MUSIC) {
        run(CommandInternal.COMMAND, MEDIA, VOLUME, listOf("0", stream.toString(), VOLUME_GET))
    }

    /**
     * Sets the volume index one step lower for a particular stream.
     * @param stream The stream whose volume index should be lower.
     * @param showUi Set to `true` to show a toast containing the current volume.
     * @see STREAM_VOICE_CALL
     * @see STREAM_SYSTEM
     * @see STREAM_RING
     * @see STREAM_MUSIC
     * @see STREAM_ALARM
     * @see STREAM_NOTIFICATION
     * @see STREAM_DTMF
     */
    fun adjustVolumeLower(stream: Int = STREAM_MUSIC, showUi: Boolean) {
        val flag = if (showUi) FLAG_VOLUME_SHOW_UI else 0
        run(CommandInternal.COMMAND, MEDIA, VOLUME, listOf(flag.toString(), stream.toString(),
                VOLUME_ADJUST_LOWER))
    }

    /**
     * Sets the volume index to the same value for a particular stream (basically does nothing).
     * @param stream The stream whose volume index should be set.
     * @param showUi Set to `true` to show a toast containing the current volume.
     * @see STREAM_VOICE_CALL
     * @see STREAM_SYSTEM
     * @see STREAM_RING
     * @see STREAM_MUSIC
     * @see STREAM_ALARM
     * @see STREAM_NOTIFICATION
     * @see STREAM_DTMF
     */
    fun adjustVolumeSame(stream: Int = STREAM_MUSIC, showUi: Boolean) {
        val flag = if (showUi) FLAG_VOLUME_SHOW_UI else 0
        run(CommandInternal.COMMAND, MEDIA, VOLUME, listOf(flag.toString(), stream.toString(),
                VOLUME_ADJUST_SAME))
    }

    /**
     * Sets the volume index one step higher for a particular stream.
     * @param stream The stream whose volume index should be raise.
     * @param showUi Set to `true` to show a toast containing the current volume.
     * @see STREAM_VOICE_CALL
     * @see STREAM_SYSTEM
     * @see STREAM_RING
     * @see STREAM_MUSIC
     * @see STREAM_ALARM
     * @see STREAM_NOTIFICATION
     * @see STREAM_DTMF
     */
    fun adjustVolumeRaise(stream: Int = STREAM_MUSIC, showUi: Boolean) {
        val flag = if (showUi) FLAG_VOLUME_SHOW_UI else 0
        run(CommandInternal.COMMAND, MEDIA, VOLUME, listOf(flag.toString(), stream.toString(),
                VOLUME_ADJUST_RAISE))
    }
}