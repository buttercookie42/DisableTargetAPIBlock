package de.buttercookie.disabletargetapiblock;

import androidx.annotation.Keep;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class DisableTargetApiBlock implements IXposedHookLoadPackage {
    @Keep
    public DisableTargetApiBlock() {}

    @Keep
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

    }
}
