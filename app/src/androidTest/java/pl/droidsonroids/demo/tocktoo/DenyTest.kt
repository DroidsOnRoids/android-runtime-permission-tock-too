package pl.droidsonroids.demo.tocktoo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.By
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import android.support.test.uiautomator.Until
import android.util.Log
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DenyTest {

    @Test
    fun deniedPermissionIsReallyGranted() {
        val timeout = 1000L

        val context = InstrumentationRegistry.getContext()
        val targetContext = InstrumentationRegistry.getTargetContext()
        assertEquals("English language is required in test", Locale.ENGLISH.language, Locale.getDefault().language)
        assertEquals("Permission must not be granted initially", PackageManager.PERMISSION_DENIED,
                targetContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        val launchIntent = context.packageManager.getLaunchIntentForPackage(BuildConfig.APPLICATION_ID)
        assertNotNull(launchIntent)
        launchIntent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(launchIntent)

        device.wait(Until.hasObject(By.pkg(BuildConfig.APPLICATION_ID).depth(0)), timeout)

        val denyButton = device.findObject(UiSelector().text("DENY"))
        assertTrue(denyButton.waitForExists(timeout))

        val uri = Uri.Builder().scheme("package").opaquePart(BuildConfig.APPLICATION_ID).build()
        val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(settingsIntent)

        val permissionsLabel = device.findObject(UiSelector().text("Permissions"))
        assertTrue(permissionsLabel.waitForExists(timeout))
        assertTrue(permissionsLabel.clickAndWaitForNewWindow())

        val storageLabel = device.findObject(UiSelector().text("Storage"))
        assertTrue(storageLabel.waitForExists(timeout))
        assertTrue(storageLabel.click())
        assertTrue(device.pressBack())
        assertTrue(device.pressBack())

        assertTrue(denyButton.click())

        val permissionState = when (targetContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PackageManager.PERMISSION_DENIED -> "denied"
            PackageManager.PERMISSION_GRANTED -> "granted"
            else -> "unknown"
        }

        Log.d("permission", "Permission state after denial: $permissionState")

        val deniedLabel = device.findObject(UiSelector().text("Permission granted"))
        assertTrue("Granted label not visible", deniedLabel.waitForExists(timeout))
    }
}
