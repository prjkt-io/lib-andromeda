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
import projekt.andromeda.client.CommandInternal.SETTINGS
import projekt.andromeda.client.CommandInternal.run

/**
 * The AndromedaSettings provides control over global system-level device preferences.
 */
object AndromedaSettings {
    private const val GET_SYSTEM = "get_system"
    private const val GET_SECURE = "get_secure"
    private const val GET_GLOBAL = "get_global"
    private const val PUT_SYSTEM = "put_system"
    private const val PUT_SECURE = "put_secure"
    private const val PUT_GLOBAL = "put_global"
    private const val DELETE_SYSTEM = "delete_system"
    private const val DELETE_SECURE = "delete_secure"
    private const val DELETE_GLOBAL = "delete_global"
    private const val LIST_SYSTEM = "list_system"
    private const val LIST_SECURE = "list_secure"
    private const val LIST_GLOBAL = "list_global"

    /**
     * System settings, containing miscellaneous system preferences.
     */
    object System {

        /**
         * Convenience function for retrieving a single system settings value
         * as a floating point number. Note that internally setting values are
         * always stored as strings; this function converts the string to a
         * float for you. The default value will be returned if the setting
         * is not defined or not a valid float and the default value is provided.
         * @param key The name of the setting to retrieve.
         * @param default Value to return if the setting is not defined or not
         * a valid float.
         * @param uid The user id to get the setting from.
         */
        fun getFloat(key: String, default: Float? = null, uid: Int? = null): Float? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result.toFloatOrNull()
            }
            return default
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a integer number. Note that internally setting values are always
         * stored as strings; this function converts the string to a integer
         * for you. The default value will be returned if the setting is not
         * defined or not a valid integer and the default value is provided.
         * @param key The name of the setting to retrieve.
         * @param default Value to return if the setting is not defined or not
         * a valid integer.
         * @param uid The user id to get the setting from.
         */
        fun getInt(key: String, default: Int? = null, uid: Int? = null): Int? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result.toIntOrNull()
            }
            return default
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a long number. Note that internally setting values are always
         * stored as strings; this function converts the string to a long
         * number for you. The default value will be returned if the setting
         * is not defined or not a valid long and the default value is provided.
         * @param key The name of the setting to retrieve.
         * @param default Value to return if the setting is not defined or not
         * a valid long.
         * @param uid The user id to get the setting from.
         */
        fun getLong(key: String, default: Long? = null, uid: Int? = null): Long? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result.toLongOrNull()
            }
            return default
        }

        /**
         * Look up for the key value in the database.
         * @param key The name of the setting to retrieve.
         * @param uid The user id to get the setting from.
         * @return The corresponding value or `null` if it's not present.
         */
        fun getString(key: String, uid: Int? = null): String? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result
            }
            return null
        }

        private fun get(key: String, uid: Int? = null): String {
            val args = if (uid != null) {
                listOf(key, uid.toString())
            } else {
                listOf(key)
            }
            return run(COMMAND, SETTINGS, GET_SYSTEM, args)
        }

        /**
         * Convenience function for updating a single settings value as a
         * floating point number. This will either create a new entry in the
         * table if the given name does not exist, or modify the value of
         * the existing row with that name. Note that internally setting
         * values are always stored as strings, so this function converts
         * the given value to a string before storing it.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putFloat(key: String, value: Float, uid: Int? = null) {
            put(key, value, uid)
        }

        /**
         * Convenience function for updating a single settings value as a
         * integer number. This will either create a new entry in the table
         * if the given name does not exist, or modify the value of the
         * existing row with that name. Note that internally setting values
         * are always stored as strings, so this function converts the given
         * value to a string before storing it.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putInt(key: String, value: Int, uid: Int? = null) {
            put(key, value, uid)
        }

        /**
         * Convenience function for updating a single settings value as a
         * long number. This will either create a new entry in the table
         * if the given name does not exist, or modify the value of the
         * existing row with that name. Note that internally setting values
         * are always stored as strings, so this function converts the
         * given value to a string before storing it.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putLong(key: String, value: Long, uid: Int? = null) {
            put(key, value, uid)
        }

        /**
         * Stores a name/value pair into the database.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putString(key: String, value: String, uid: Int? = null) {
            put(key, value, uid)
        }

        private fun put(key: String, value: Any, uid: Int? = null) {
            val args = if (uid != null) {
                listOf(key, value.toString(), uid.toString())
            } else {
                listOf(key, value.toString())
            }
            run(COMMAND, SETTINGS, PUT_SYSTEM, args)
        }

        /**
         * Deletes a name/value pair from the database.
         * @param key The name of the setting to delete.
         */
        fun delete(key: String) {
            run(COMMAND, SETTINGS, DELETE_SYSTEM, listOf(key))
        }

        /**
         * Returns a map of name/value pair of the System settings.
         */
        fun list(): Map<String, String> {
            val out = HashMap<String, String>()
            val cmdOut = run(COMMAND, SETTINGS, LIST_SYSTEM, listOf()).lines()
            cmdOut.forEach {
                val lineSplit = it.split("=", limit = 2)
                if (lineSplit.size >= 2) {
                    out[lineSplit[0]] = lineSplit[1]
                }
            }
            return out
        }
    }

    /**
     * Secure system settings, containing system preferences.
     */
    object Secure {
        /**
         * Convenience function for retrieving a single system settings value
         * as a floating point number. Note that internally setting values are
         * always stored as strings; this function converts the string to a
         * float for you. The default value will be returned if the setting
         * is not defined or not a valid float and the default value is provided.
         * @param key The name of the setting to retrieve.
         * @param default Value to return if the setting is not defined or not
         * a valid float.
         * @param uid The user id to get the setting from.
         */
        fun getFloat(key: String, default: Float? = null, uid: Int? = null): Float? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result.toFloatOrNull()
            }
            return default
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a integer number. Note that internally setting values are always
         * stored as strings; this function converts the string to a integer
         * for you. The default value will be returned if the setting is not
         * defined or not a valid integer and the default value is provided.
         * @param key The name of the setting to retrieve.
         * @param default Value to return if the setting is not defined or not
         * a valid integer.
         * @param uid The user id to get the setting from.
         */
        fun getInt(key: String, default: Int? = null, uid: Int? = null): Int? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result.toIntOrNull()
            }
            return default
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a long number. Note that internally setting values are always
         * stored as strings; this function converts the string to a long
         * number for you. The default value will be returned if the setting
         * is not defined or not a valid long and the default value is provided.
         * @param key The name of the setting to retrieve.
         * @param default Value to return if the setting is not defined or not
         * a valid long.
         * @param uid The user id to get the setting from.
         */
        fun getLong(key: String, default: Long? = null, uid: Int? = null): Long? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result.toLongOrNull()
            }
            return default
        }

        /**
         * Look up for the key value in the database.
         * @param key The name of the setting to retrieve.
         * @param uid The user id to get the setting from.
         * @return The corresponding value or `null` if it's not present.
         */
        fun getString(key: String, uid: Int? = null): String? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result
            }
            return null
        }

        private fun get(key: String, uid: Int? = null): String {
            val args = if (uid != null) {
                listOf(key, uid.toString())
            } else {
                listOf(key)
            }
            return run(COMMAND, SETTINGS, GET_SECURE, args)
        }

        /**
         * Convenience function for updating a single settings value as a
         * floating point number. This will either create a new entry in the
         * table if the given name does not exist, or modify the value of
         * the existing row with that name. Note that internally setting
         * values are always stored as strings, so this function converts
         * the given value to a string before storing it.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putFloat(key: String, value: Float, uid: Int? = null) {
            put(key, value, uid)
        }

        /**
         * Convenience function for updating a single settings value as a
         * integer number. This will either create a new entry in the table
         * if the given name does not exist, or modify the value of the
         * existing row with that name. Note that internally setting values
         * are always stored as strings, so this function converts the given
         * value to a string before storing it.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putInt(key: String, value: Int, uid: Int? = null) {
            put(key, value, uid)
        }

        /**
         * Convenience function for updating a single settings value as a
         * long number. This will either create a new entry in the table
         * if the given name does not exist, or modify the value of the
         * existing row with that name. Note that internally setting values
         * are always stored as strings, so this function converts the
         * given value to a string before storing it.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putLong(key: String, value: Long, uid: Int? = null) {
            put(key, value, uid)
        }

        /**
         * Stores a name/value pair into the database.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putString(key: String, value: String, uid: Int? = null) {
            put(key, value, uid)
        }

        private fun put(key: String, value: Any, uid: Int? = null) {
            val args = if (uid != null) {
                listOf(key, value.toString(), uid.toString())
            } else {
                listOf(key, value.toString())
            }
            run(COMMAND, SETTINGS, PUT_SECURE, args)
        }

        /**
         * Deletes a name/value pair from the database.
         * @param key The name of the setting to delete.
         */
        fun delete(key: String) {
            run(COMMAND, SETTINGS, DELETE_SECURE, listOf(key))
        }

        /**
         * Returns a map of name/value pair of the System settings.
         */
        fun list(): Map<String, String> {
            val out = HashMap<String, String>()
            val cmdOut = run(COMMAND, SETTINGS, LIST_SECURE, listOf()).lines()
            cmdOut.forEach {
                val lineSplit = it.split("=", limit = 2)
                if (lineSplit.size >= 2) {
                    out[lineSplit[0]] = lineSplit[1]
                }
            }
            return out
        }
    }

    /**
     * Global system settings, containing preferences that always apply
     * identically to all defined users.
     */
    object Global {
        /**
         * Convenience function for retrieving a single system settings value
         * as a floating point number. Note that internally setting values are
         * always stored as strings; this function converts the string to a
         * float for you. The default value will be returned if the setting
         * is not defined or not a valid float and the default value is provided.
         * @param key The name of the setting to retrieve.
         * @param default Value to return if the setting is not defined or not
         * a valid float.
         * @param uid The user id to get the setting from.
         */
        fun getFloat(key: String, default: Float? = null, uid: Int? = null): Float? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result.toFloatOrNull()
            }
            return default
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a integer number. Note that internally setting values are always
         * stored as strings; this function converts the string to a integer
         * for you. The default value will be returned if the setting is not
         * defined or not a valid integer and the default value is provided.
         * @param key The name of the setting to retrieve.
         * @param default Value to return if the setting is not defined or not
         * a valid integer.
         * @param uid The user id to get the setting from.
         */
        fun getInt(key: String, default: Int? = null, uid: Int? = null): Int? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result.toIntOrNull()
            }
            return default
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a long number. Note that internally setting values are always
         * stored as strings; this function converts the string to a long
         * number for you. The default value will be returned if the setting
         * is not defined or not a valid long and the default value is provided.
         * @param key The name of the setting to retrieve.
         * @param default Value to return if the setting is not defined or not
         * a valid long.
         * @param uid The user id to get the setting from.
         */
        fun getLong(key: String, default: Long? = null, uid: Int? = null): Long? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result.toLongOrNull()
            }
            return default
        }

        /**
         * Look up for the key value in the database.
         * @param key The name of the setting to retrieve.
         * @param uid The user id to get the setting from.
         * @return The corresponding value or `null` if it's not present.
         */
        fun getString(key: String, uid: Int? = null): String? {
            val result = get(key, uid)
            if (result.isNotEmpty()) {
                return result
            }
            return null
        }

        private fun get(key: String, uid: Int? = null): String {
            val args = if (uid != null) {
                listOf(key, uid.toString())
            } else {
                listOf(key)
            }
            return run(COMMAND, SETTINGS, GET_GLOBAL, args)
        }

        /**
         * Convenience function for updating a single settings value as a
         * floating point number. This will either create a new entry in the
         * table if the given name does not exist, or modify the value of
         * the existing row with that name. Note that internally setting
         * values are always stored as strings, so this function converts
         * the given value to a string before storing it.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putFloat(key: String, value: Float, uid: Int? = null) {
            put(key, value, uid)
        }

        /**
         * Convenience function for updating a single settings value as a
         * integer number. This will either create a new entry in the table
         * if the given name does not exist, or modify the value of the
         * existing row with that name. Note that internally setting values
         * are always stored as strings, so this function converts the given
         * value to a string before storing it.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putInt(key: String, value: Int, uid: Int? = null) {
            put(key, value, uid)
        }

        /**
         * Convenience function for updating a single settings value as a
         * long number. This will either create a new entry in the table
         * if the given name does not exist, or modify the value of the
         * existing row with that name. Note that internally setting values
         * are always stored as strings, so this function converts the
         * given value to a string before storing it.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putLong(key: String, value: Long, uid: Int? = null) {
            put(key, value, uid)
        }

        /**
         * Stores a name/value pair into the database.
         * @param key The name of the setting to modify.
         * @param value The new value for the setting.
         * @param uid The user id to put the new settings.
         */
        fun putString(key: String, value: String, uid: Int? = null) {
            put(key, value, uid)
        }

        private fun put(key: String, value: Any, uid: Int? = null) {
            val args = if (uid != null) {
                listOf(key, value.toString(), uid.toString())
            } else {
                listOf(key, value.toString())
            }
            run(COMMAND, SETTINGS, PUT_GLOBAL, args)
        }

        /**
         * Deletes a name/value pair from the database.
         * @param key The name of the setting to delete.
         */
        fun delete(key: String) {
            run(COMMAND, SETTINGS, DELETE_GLOBAL, listOf(key))
        }

        /**
         * Returns a map of name/value pair of the System settings.
         */
        fun list(): Map<String, String> {
            val out = HashMap<String, String>()
            val cmdOut = run(COMMAND, SETTINGS, LIST_GLOBAL, listOf()).lines()
            cmdOut.forEach {
                val lineSplit = it.split("=", limit = 2)
                if (lineSplit.size >= 2) {
                    out[lineSplit[0]] = lineSplit[1]
                }
            }
            return out
        }
    }
}