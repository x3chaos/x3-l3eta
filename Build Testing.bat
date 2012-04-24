@ECHO OFF
cd bin/
set directory=com\
java org.x3.bukkit.main.Builder TestingPlugin TestingPlugin %directory% Testing org\x3\util org\x3\bukkit\testing\ org\l3eta\npclib\
pause