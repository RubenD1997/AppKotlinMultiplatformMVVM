package org.example.project

import org.example.project.data.CrossConfigDevice
import platform.UIKit.UITraitCollection
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

class CrossConfigDevice() : CrossConfigDevice {

    override fun isDarkModeEnabled(): Boolean {
        val osTheme: UITraitCollection = UIScreen.mainScreen.traitCollection
        return osTheme.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    }

}