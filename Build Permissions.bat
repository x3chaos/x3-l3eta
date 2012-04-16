@ECHO OFF
cd bin/
set directory=org\x3\bukkit\permissions\
java org.x3.bukkit.main.Builder PermissionPlugin x3Permissions %directory% org\l3eta org\x3\util org\x3
pause