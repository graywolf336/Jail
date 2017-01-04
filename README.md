[Jail 3.0](http://ci.graywolf336.com/job/Jail/) - [JavaDoc](http://ci.graywolf336.com/job/Jail/javadoc) - [Wiki](https://github.com/graywolf336/Jail/wiki) - [Translate](https://translate.lingohub.com/craftyn/jail-plugin/dashboard)
====
This plugins adds Jail to your Minecraft server. Admins can define several jails and then jail/unjail people, can be on a time basis. This plugin also offers wide variety of protections, this way players won't escape out of jail.

[![Build Status](http://ci.graywolf336.com/job/Jail/badge/icon)](http://ci.graywolf336.com/job/Jail/) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/7934cceb4d27488f99ccba52f30681d1)](https://www.codacy.com/app/graywolf336/Jail?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=graywolf336/Jail&amp;utm_campaign=Badge_Grade)

Translating
===
If you would like to help translate this project, please shoot me an email `graywolf336`@`craftyn.com` and saying which language(s) you would like to contribute to and I will add you.

Developing/Building
===
If you want to make some changes, build, and run the unit tests you will notice we require CraftBukkit 1.8 with maven and it isn't hosted anywhere publically for legal reasons. You will need to build CraftBukkit 1.8 yourself and then put it locally for maven with the following command:

`mvn install:install-file -Dfile=craftbukkit-1.9-R0.1.jar -DgroupId=org.bukkit -DartifactId=craftbukkit -Dversion=1.9-R0.1-SNAPSHOT -Dpackaging=jar`

Beta 6 Changes
*Changes since Beta 5*

[Beta 5](https://github.com/graywolf336/Jail/releases/tag/v3.0.0-beta.5) Changes
===
*Changes since Beta 4*
* Added a command to update, verify, refresh, and clean signs on cells. [#79](https://github.com/graywolf336/Jail/issues/79)
* Added an api to get the build number, this way we can reference it in the future.
* Added a password requirement on the broadcast of jailing, defaults to everyone. [#54](https://github.com/graywolf336/Jail/issues/54)
* Added the ability to show `reason`, `jail`, and `cell` to the broadcast messages. [#53](https://github.com/graywolf336/Jail/issues/53)
* Added tab complete to all the commands. [#77](https://github.com/graywolf336/Jail/issues/77)
* Added reloading the move protection listener, so the reload actually loads/disables it. [#88](https://github.com/graywolf336/Jail/issues/88)
* Changed cell names to be semi-sorted alphabetically. [#80](https://github.com/graywolf336/Jail/issues/80)
* Changed how we handle inventory when storing is set to false. Don't remove their inventory when they are unjailed and we don't store it. [#57](https://github.com/graywolf336/Jail/issues/57)
* Changed offline players jailing, don't allow jailing players unless they've played before (can be overwrote with -f). [#82](https://github.com/graywolf336/Jail/issues/82)
* Changed the jail api, see [#72's comment](https://github.com/graywolf336/Jail/issues/72#issuecomment-104757472) for some details.
* Changed jailing items, we run all jailing sync so that any api usage async won't cause problems. [#73](https://github.com/graywolf336/Jail/issues/73)
* Changed the format of the jail check command, thanks to stevoh6. [#65](https://github.com/graywolf336/Jail/pull/65)
* Changed the explanation of why the gamemode setting was problematic and give the available options. [#73](https://github.com/graywolf336/Jail/issues/73)
* Changed pretty time to be the default of the signs.
* Fixed a sqlite issue which was preventing plugin from launching. [#78](https://github.com/graywolf336/Jail/issues/78)
* Fixed a sqlite error when deleting a cell. [#89](https://github.com/graywolf336/Jail/issues/89)
* Fixed an issue where cell data was being duplicated (or more) in the database. [#74](https://github.com/graywolf336/Jail/issues/74)
* Fixed an on load issue when the config didn't have four lines for the signs. [#61](https://github.com/graywolf336/Jail/issues/61)
* Fixed cell signs not updating when an offline player is jailed. [#68](https://github.com/graywolf336/Jail/issues/68)
* Fixed chests being selectable if trapped and regular were next to each other.
* Fixed inventory not being stored in chests, trapped chests and chests work just fine now.
* Fixed jail names being case sensitive. [#76](https://github.com/graywolf336/Jail/issues/76)
* Fixed jail sticks not putting players into cells. [#68](https://github.com/graywolf336/Jail/issues/68)
* Fixed pretty time not working with jailed forever time.
* Fixed respawning after dying not placing players back into their cells when another plugin sets their respawn point. [#55](https://github.com/graywolf336/Jail/issues/55)
* Fixed time being added/subtracted from a player's time when they were jailed forever, resulting in them being able to get out. [#69](https://github.com/graywolf336/Jail/issues/69)
* Fixed transferring players who've never been on before not working. [#83](https://github.com/graywolf336/Jail/issues/83)
* Fixed signs disappearing after reloading the plugin. [#67](https://github.com/graywolf336/Jail/issues/67)
* Fixed signs not updating after transferring someone.
* Fixed storage not changing when reloading the plugin, allows converting from one type to another. [#75](https://github.com/graywolf336/Jail/issues/75)
* Fixed subcommands of `/jail` being case sensitive.

[Beta 4](https://github.com/graywolf336/Jail/releases/tag/v3.0.0-beta.4) Changes
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
