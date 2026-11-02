# 实验四 翻牌游戏

袁连一 2024110545。使用 Android Studio 打开本目录即可。

三个 Activity：玩家信息、资料修改、16张牌翻牌游戏。资料只保存在内存中。模型沿用附件 Kotlin 代码。卡牌采用57:87竖向比例，外观参考姚记蓝白牌背与传统人物牌重绘，点数、花色及双向角标由模型数据绘制。没有实验五内容。

构建与测试：

```powershell
./gradlew.bat assembleDebug testDebugUnitTest lintDebug assembleDebugAndroidTest
# 连接设备后执行 UI 测试
./gradlew.bat connectedDebugAndroidTest
```

已通过 APK 构建、4项单元测试和 UI 测试包编译。设备端 UI 测试尚未完成。项目暂不上传，报告另行补充。

图片素材：`app/src/main/res/drawable-nodpi/yaoji_cards.png`，由内置 ImageGen 根据姚记产品图片参考重绘，并非官方扫描素材。提示词要求四列图集（蓝白牌背、J、Q、K），人物画不带角标，角标由程序绘制。参考图片：https://tao.hooos.com/goods_647749677300.html 、https://vipssl.ey100.com/Goods/ItemDetail_8028-0739_1.htm 。
