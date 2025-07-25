package com.teebay.appname.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teebay.appname.R
import okio.Options

fun Context.showSimpleDialog(
    title: String? = null,
    message: String = "",
    cancelable: Boolean = false,
    positiveBtnLabel: String = "Yes",
    negativeBtnLabel: String = "No",
    onPositiveClick: (() -> Unit)? = null,
    onNegativeClick: (() -> Unit)? = null
) {
    val dialog = AlertDialog
        .Builder(this)
        .setMessage(message)
        .setPositiveButton(positiveBtnLabel) { dialog, _ ->
            onPositiveClick?.invoke()
            dialog.dismiss()
        }
        .setNegativeButton(negativeBtnLabel) { dialog, _ ->
            onNegativeClick?.invoke()
            dialog.dismiss()
        }
        .setCancelable(cancelable)
        .create()

    title?.let { dialog.setTitle(it) }

    dialog.show()
}

fun Context.showCustomDialog(
    view: View,
    onViewCreated: (() -> Unit)? = null,
    cancelable: Boolean = false,
    positiveBtnLabel: String = "Yes",
    negativeBtnLabel: String = "No",
    onPositiveClick: (() -> Unit)? = null,
    onNegativeClick: (() -> Unit)? = null
): AlertDialog {
    val dialog = AlertDialog
        .Builder(this)
        .setView(view)
        .setPositiveButton(positiveBtnLabel) { dialog, _ ->
            onPositiveClick?.invoke()
            dialog.dismiss()
        }
        .setNegativeButton(negativeBtnLabel) { dialog, _ ->
            onNegativeClick?.invoke()
            dialog.dismiss()
        }
        .setCancelable(cancelable)
        .create()

    onViewCreated?.invoke()

    return dialog
}

fun Context.showOptionDialog(
    title: String = "",
    options: Array<String>,
    onItemClick: (Int) -> Unit,
) {
    AlertDialog
        .Builder(this)
        .setTitle(title)
        .setItems(options) { _, which ->
            onItemClick(which)
        }
        .show()
}

fun Context.showLoaderDialog(
    activity: Activity
): AlertDialog {
    val dialog = this
        .showCustomDialog(
            view = LayoutInflater
                .from(this)
                .inflate(R.layout.dialog_loading, activity.window.decorView as ViewGroup, false)
        )

    return dialog
}