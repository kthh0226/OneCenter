新的OneBoard到手，研究了下如何直接用于android开发调试。
经过一些尝试后，总结方案有2种。

第一种是OneBoard直接接显示器使用的情况下，使用usb线。

如果使用usb线的话，需要使用双头都是usb口的线与电脑连接，如图：

并且在oneBoard的设置中打开usb调试选项，这样在电脑中，通过adb devices就可以查看到oneboard了。



第二种是OneBoard作为键盘使用，连接电脑机箱的情况下，使用adb wifi调试

使用adb wifi进行调试的话，好处就是可以免去再单独购买的那根usb线，缺点就是设备必须root。

各大应用市场上，有各种的adb wifi软件，有的还内置广告插件，有的bug很多，这里经过实测，建议使用

应用宝上的adbWireless，程序比较稳定，也没有广告插件，开启adbWireless后，使用命令adb connect 设备ip就可以连接调试。

