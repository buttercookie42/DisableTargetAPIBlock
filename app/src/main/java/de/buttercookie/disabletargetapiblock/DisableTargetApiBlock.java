/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package de.buttercookie.disabletargetapiblock;

import static de.robv.android.xposed.XposedHelpers.findMethodExactIfExists;
import static de.robv.android.xposed.XposedHelpers.getIntField;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.setIntField;

import androidx.annotation.Keep;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class DisableTargetApiBlock implements IXposedHookLoadPackage {
    private static final String LOGTAG = "DisableTargetApiBlock";

    private static final String CLASS_INSTALL_PACKAGE_HELPER = "com.android.server.pm.InstallPackageHelper";
    private static final String CLASS_INSTALL_REQUEST = "com.android.server.pm.InstallRequest";
    private static final int INSTALL_BYPASS_LOW_TARGET_SDK_BLOCK = 0x01000000;

    @Keep
    public DisableTargetApiBlock() {}

    @Keep
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        switch (lpparam.packageName) {
            case "android":
                hookFramework(lpparam);
                break;
        }
    }

    private static void hookFramework(XC_LoadPackage.LoadPackageParam lpparam) {
        Method method = findMethodExactIfExists(CLASS_INSTALL_PACKAGE_HELPER, lpparam.classLoader,
                "preparePackageLI", CLASS_INSTALL_REQUEST);
        if (method == null) {
            // Method name changed between Android 15.0.0_r17 and r20
            method = findMethodExactIfExists(CLASS_INSTALL_PACKAGE_HELPER, lpparam.classLoader,
                    "preparePackage", CLASS_INSTALL_REQUEST);
        }
        if (method == null) {
            XposedBridge.log(LOGTAG + ": method to hook not found");
            return;
        }

        XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Object request = param.args[0];
                        Object installArgs = getObjectField(request, "mInstallArgs");
                        if (installArgs == null) {
                            return;
                        }

                        int installFlags = getIntField(installArgs, "mInstallFlags");
                        installFlags |= INSTALL_BYPASS_LOW_TARGET_SDK_BLOCK;
                        setIntField(installArgs, "mInstallFlags", installFlags);
                    }
                });
    }
}
