[Jail 3.0](http://ci.graywolf336.com/job/Jail/)
====
This plugins adds Jail to your Minecraft server. Admins can define several jails and then jail/unjail people or jail them on time basis. Plugin also offers wide variety of protections, so players won't escape out of your jail.

**All mysql queries and actions are done sync, meaning if the connection to the mysql server is slow/down this will probably end up blocking the server. There are plans to change this, as we don't want to slow down anyone's servers.**

[![Build Status](http://ci.graywolf336.com/job/Jail/badge/icon)](http://ci.graywolf336.com/job/Jail/)

Beta 2 Changes
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

Beta 1 Changes
===
*Changes since alpha*
* MySQL storage is now a valid option for storage ([#18](https://github.com/graywolf336/Jail/issues/18))
* MySQL data validation, basically if a cell or prisoner reference a jail which no longer exists they are removed
* Fix a bug with not being able to unjail someone forcefully if they were in a cell ([#17](https://github.com/graywolf336/Jail/issues/17))
* Add the record keeping system ([#12](https://github.com/graywolf336/Jail/issues/12))
* Added Jail Sticks, format of them has slightly changed ([#16](https://github.com/graywolf336/Jail/issues/16))

Changes
===
*With this being version 3 of this plugin, a couple things have changed but the core concept is still the exact same. Here are some drastic changes:*
* Time can now be entered different formats, 3hours or 15mins or 4days
* New language system
* New config system (per jail configs are going to make a come back)
* Items in the config use item names now, **not** the ids
* All commands are prevented by default, now there is **only** a whitelist
* All interactions are prevented by default, add to the whitelist what you want
* If store inventory is not enabled, inventories are deleted upon jailing
* Sign text has a new format, old format will not be converted
* Max reason length has been removed, might be added back in if needed

ToDo
===
* When calculating the reducing of time, make it async and all the calls it does be scheduled
* Jail set
* Jail vote
* Jailing for swearing
* Guards (PlayerMoveProtectionAction - when they try to move do we teleport them back, let the guards get them, or nothing)
* Storing permissions
* Update Notifications
* Pages on jail list

Notice
===
* Old messages (language) will not be converted
* MaximumAFKTime setting will not convert over, the format isn't clear and the old version didn't provide a way to get values with decimal places
* EnableLogging has been removed, we are always going to be logging (unless major request to control this)
* Prisoner's old inventory strings in the database are lost, we can not convert those

[Jail 3.0 JavaDoc](http://ci.graywolf336.com/job/Jail/javadoc)
====
