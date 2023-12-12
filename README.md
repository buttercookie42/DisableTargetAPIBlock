# Disable Target API Block
On Android 14, Google started blocking old apps (those targeting a target API lower than 23, i.e.
anything before Marshmallow/Android 6) from being installed.

While this behaviour can be overridden by calling `adb install` (respectively `pm install` if
you've got access to a root shell on your device) with the `--bypass-low-target-sdk-block` flag, it
still breaks simple manual installation of older APKs, as well as any backup apps and other software
like that which hasn't yet been updated for this new behaviour.

This Xposed module hooks the app installation process to always behave as if that flag had been
passed and to therefore generally allow installation of old apps.

## Compatibility
Requires at least Android 14.
