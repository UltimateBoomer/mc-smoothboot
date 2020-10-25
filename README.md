# Lagless Boot (Fabric)
## Make Minecraft not slow down your system whening loading into the title screen.

If you have a PC with a lower thread count CPU (ex. 4 core 8 threads), you may have noticed that Minecraft takes up 100% of the CPU when loading into the title screen. As a result, doing anything else on the computer at the same time is next to impossible.
This is because Minecraft will try to use all of your CPU threads to load the game, which while it speeds up the loading process it also takes up processing cycles from other programs. Note that the threads used is limited to 7 max plus the main thread by vanilla default, so if you have a higher core count CPU you probably won't notice the slowdown.
This mod alleviates this problem by reducing the number of threads and their priority. Note that this mod will likely slow down the load times, but not by a large margin unless you lower the thread usage even further. The mod will not affect anything after the game is done loading, so it won't reduce in game performance. The mod provides a config file for further tweaking.
Alternatively, it's possible to raise the threads used above Minecraft's default maximum, so setting it to a higher value will in theory improve game load times on CPUs with more cores. Testing is needed for this as I don't own a CPU with more than 4 cores.
