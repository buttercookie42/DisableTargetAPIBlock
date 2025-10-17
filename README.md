# Disable Target API Block
On Android 14, Google started blocking old apps (those targeting a target API lower than 23, i.e.
anything before Marshmallow/Android 6) from being installed.

While this behaviour can be overridden by calling `adb install` (respectively `pm install` if
you've got access to a root shell on your device) with the `--bypass-low-target-sdk-block` flag, it
still breaks simple manual installation of older APKs, as well as any backup apps and other software
like that which hasn't yet been updated for this new behaviour.

This Xposed module hooks the app installation process to always behave as if that flag had been
passed and to therefore generally allow installation of old apps.

## F-Droid support
This module can also be enabled for the official F-Droid client, which will override F-Droid's
compatibility checking so that old apps no longer appear as incompatible on modern devices.

For technical reasons, you currently need to clear F-Droid's app data in order to reset the
compatibility data for all old apps after enabling this module, so make sure make a backup and to
note down any custom repositories you added as well as any relevant setting custom settings.

Alternatively, you can just
delete `/data/data/org.fdroid.fdroid/databases/fdroid_db*`, but this will still lose all custom
repositories you added as well as any custom repository and individual app update settings.

## Compatibility
Requires at least Android 14.
