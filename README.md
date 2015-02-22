[Jail 3.0](http://ci.graywolf336.com/job/Jail/) - [JavaDoc](http://ci.graywolf336.com/job/Jail/javadoc) - [Wiki](https://github.com/graywolf336/Jail/wiki)
====
This plugins adds Jail to your Minecraft server. Admins can define several jails and then jail/unjail people, can be on a time basis. This plugin also offers wide variety of protections, this way players won't escape out of jail.

[![Build Status](http://ci.graywolf336.com/job/Jail/badge/icon)](http://ci.graywolf336.com/job/Jail/)

Developing/Building
===
If you want to make some changes, build, and run the unit tests you will notice we require CraftBukkit 1.8 with maven and it isn't hosted anywhere publically for legal reasons. You will need to build CraftBukkit 1.8 yourself and then put it locally for maven with the following command:

`mvn install:install-file -Dfile=craftbukkit-1.8.jar -DgroupId=org.bukkit -DartifactId=craftbukkit -Dversion=1.8-R0.1-SNAPSHOT -Dpackaging=jar`

Beta 4 Changes
===
*Changes since Beta 3*
* Added `/jail vote`. [Jail Vote Wiki Page](https://github.com/graywolf336/Jail/wiki/Jail-Vote). [#8](https://github.com/graywolf336/Jail/issues/8)
* Added support for `%prettytime%` on signs, formats the time with `1h4m20s`. [#35](https://github.com/graywolf336/Jail/issues/35)
* Added temporary help command, see the wiki for *wip* information: https://github.com/graywolf336/Jail/wiki/
* Added the ability to set a prisoner's time with `/jail time set [player] [amount]`
* Fixed updating signs throwing an error on Spigot. [#36](https://github.com/graywolf336/Jail/issues/36)
* Fixed incorrect permissions for `/jail listcells`. [#39](https://github.com/graywolf336/Jail/issues/39)
* Fixed some permissions not being included in `jail.command.*`. [#38](https://github.com/graywolf336/Jail/issues/38)
* Fixed teleport free not sending the prisoner to the correct world.
* Fixed jailed players not being able to destory and place whitelisted blocks. [#34](https://github.com/graywolf336/Jail/issues/34)
* Fixed the plugin being loaded before the worlds, if using Multiverse-Core. [#41](https://github.com/graywolf336/Jail/issues/41)
* Fixed automatically jailing to open cells missing. [#42](https://github.com/graywolf336/Jail/issues/42)
* Fixed capital letters being ignore when creating cells. [#47](https://github.com/graywolf336/Jail/issues/47)
* Fixed some cells not being created when a chest wasn't defined. [#46](https://github.com/graywolf336/Jail/issues/46)
* Fixed not being able to jail someone for an infinite amount of time. [#49](https://github.com/graywolf336/Jail/issues/49)

[Beta 3](https://github.com/graywolf336/Jail/releases/tag/v3.0.0-beta.3) Changes
===
*Changes since Beta 2*
* Added support for sqlite storage
* Add advanced update notifications, even for beta builds
* Move cellcreate command to createcell
* Fix an out of bounds exception when using old jail stick configurations
* Make the timer async, this helps performance when you have a ton of prisoners
* Lots of work on unit testing
* Changed the encoding of the project in maven to utf8
* Fixed the language system not copying over new values
* Fixed confirmations not expiring, ever
* Commands clear and clear force have been combined, use -f (-force) if you want to forcefully clear

[Beta 2](https://github.com/graywolf336/Jail/releases/tag/v3.0.0-beta.2) Changes
===
*Changes since Beta 1*
* Using UUID as the only means of knowing if someone is jailed or not, last known username is stored for commands
* Jailings via jail sticks are now recorded, [#20](https://github.com/graywolf336/Jail/issues/20)
* Fix the default Jail Stick not being loaded correctly, [#21](https://github.com/graywolf336/Jail/issues/21)
* Implement Scoreboards, with title and time configurable. ([#15](https://github.com/graywolf336/Jail/issues/15))
* Implemented Jail Pay [#11](https://github.com/graywolf336/Jail/issues/11)
* Convert old data and config values, only some are done and if you don't want the old data delete your `global.yml`
* Add config option to disallow the usage of Jail Sticks
* Added `/jail time` for easy access to adding/subtracting time - [Bukkit Dev Ticket #432](http://dev.bukkit.org/bukkit-plugins/jail/tickets/432/)
* Added `/togglejaildebug` for easily toggling the debugging state, enable if you have a problem and want to send me information
* Added some caching of online prisoners and where they're located at, this improves performance on servers with 500+ prisoners jailed
* Only updating prisoners in the database if they were changed, this should help improve saving speed

[Beta 1](https://github.com/graywolf336/Jail/releases/tag/v3.0.0-beta.1) Changes
===
*Changes since alpha*
* MySQL storage is now a valid option for storage ([#18](https://github.com/graywolf336/Jail/issues/18))
* MySQL data validation, basically if a cell or prisoner reference a jail which no longer exists they are removed
* Fix a bug with not being able to unjail someone forcefully if they were in a cell ([#17](https://github.com/graywolf336/Jail/issues/17))
* Add the record keeping system ([#12](https://github.com/graywolf336/Jail/issues/12))
* Added Jail Sticks, format of them has slightly changed ([#16](https://github.com/graywolf336/Jail/issues/16))

To Do
===
* Jail set
* Jailing for swearing
* Guards (PlayerMoveProtectionAction - when they try to move do we teleport them back, let the guards get them, or nothing)
* Storing permissions
* Pages on jail list
