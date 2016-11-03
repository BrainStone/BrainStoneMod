v3.3.32 BETA DEV
----------------

- Improved config (if you have changed values in the config have a look at the config since it might no longe use them (The old values are still there))
- All settings now have a better message in the Config GUI
- Fixed config GUI not changing the values
- Fixed Brain Stone Life Capacitor upgrade recipes not showing in JEI
- Properly checking for Draconic Evolution
- When draconic evolution is present the Brain Stone Life Capacitor is crafted with a dragon heart
- Fixed server side crash
- Added a missing translation
- A lot of internal improvements

v3.2.2 BETA DEV
---------------

- The Dark Steel Upgrade is being rendered again
- Improved texture animations
- Increased drop chance of the Essence of Life
- Drop chance can be manipulated in the config
- Drop chance gets reduced when Draconic Evolution is present

v3.1.14 BETA
------------

- Added tool materials to Tinker's Construct (please give feedback on the balance)
- Increased mining levels of tool materials
- A few tiny fixes and improvements
- Cleaned repo
- Prepared Draconic Evolution support (good stuff to come!)

v3.1.3 BETA
-----------

- Using Forge energy system for the Brain Stone Life Capacitor (which means it will work with most energy mods)

v3.0.44 BETA
------------

- Updated to 1.10.2
- BrainStone Trigger goes full camo now (temporarily, unless users would rather it remain this way)
- Added Overlord support
- Added JEI support
- Removed NEI support
- Temporarily disabled Thaumcraft, TiCon, and MFR support
- Started using the Forge JSON update system
- Fully removed the previously dummied out Brain Logic Block
- Lowercased the modid

v2.57.40 BETA release
---------------------

- Added Extra Utilities support:
- The Reinforced Watering can can be crafted with a Essence of Life
- Fixed Config GUI not changing the config values
- Fixed server side crash related to the Pulsating Brain Stone

v2.57.34 BETA release
---------------------

Additions:
- Added NEI Support
- Added a Creative Tab
- Added an Achievement Page
- Added a BrainStone Thaumcraft aspect
- Added recipes for BrainStone in EnderIO machines
- Added full OreDictionary support
- Added a BrainStone upgrade for EnderIO Dark Steel Armor (Protects from pulsating brainstone)
- Added the BrainStone Life Capacitor (Absorbs damage as long as it has power) (Equippable as a Bauble) (Has an achievement)
- Added Stable Pulsating BrainStone (+Tools, Armor, etc)
- Added Enable/Disable All buttons to the BrainStone Trigger GUI
Changes:
- BrainStone Ore now works with the Mining Laser from Minefactory Reloaded
- BrainStone is now a TiCon material
- You can now configure whitelisted dimensions for BrainStone Ore and House
- Brain Logic Block has been dummied out. Place it in the world and right click it to get your ingredients back.
- Removed "X" button from the GUIs
- No longer depends on EnderIO
- Many improvements and bug fixes.

v2.57.13 BETA prerelease
------------------------

Additions:
- Added NEI Support
- Added a Creative Tab
- Added an Achievement Page
- Added a BrainStone Thaumcraft aspect
- Added recipes for BrainStone in EnderIO machines
- Added full OreDictionary support
- Added a BrainStone upgrade for EnderIO Dark Steel Armor (Protects from pulsating brainstone)
- Added the BrainStone Life Capacitor (Absorbs damage as long as it has power) (Equippable as a Bauble)
- Added Stable Pulsating BrainStone (+Tools, Armor, etc)
- Added Enable/Disable All buttons to the BrainStone Trigger GUI
Changes:
- BrainStone Ore now works with the Mining Laser from Minefactory Reloaded
- BrainStone is now a TiCon material
- You can now configure whitelisted dimensions for BrainStone Ore and
House
- Brain Logic Block has been dummied out. Place it in the world and right click it to get your ingredients back.
- Removed â€œXâ€ button from the GUIs
- Many improvements and bug fixes.


v2.56.637 BETA DEV
------------------

- Added NEI support for BrainStoneLifeCapacitor Upgrades
- Optimizations and cleanups


v2.56.561 BETA DEV
------------------

- Also using Forge jar verification
- Removed the Brain Logic Block. Place any existing ones in the world and break them to get your crafting ingredients back.
- Use OreDictionary wherever possible
- Fixed BrainStone Trigger GUI being extremely buggy
- Rewrote packet handling. Client-Server synchronization works much better now
- Many internal improvements


v2.53.312 BETA DEV
------------------

- In-game config now supported
- Improved signature verification
- Reduced debug messages in console


v2.53.229 BETA DEV
------------------

- Improved config
- BrainStoneLifeCapacitor can no longer be stolen by default (can be changed in config)
- How much RF half a heart costs for the BrainStoneLifeCapacitor can be changed in the config


v2.53.151 BETA DEV
------------------

- Fixed spelling errors in the BrainStoneLifeCapacitor and the EssenceOfLive


v2.53.143 BETA DEV
------------------

- Fixed jar signing


v2.53.137 BETA DEV
------------------

- Fixed yet another bug


v2.53.117 BETA DEV
------------------

- Fixed small bug


v2.53.115 BETA DEV
------------------

- Fixed some spelling errors
- Better jar verification: Using signed jars now instead of verifying through the download server. This also allows to verify custom builds. A propper warning system is still WIP!


v2.52.159 BETA DEV
------------------

- Fixed using wrong tag type for the energy


v2.52.156 BETA DEV
------------------

- Fixed #13
- Using correct URL now


v2.51.543 BETA DEV
------------------

- Created DEV-Build


v2.51.489 BETA DEV
------------------

- Fixed long standing serverside crash with the BrainLogicBlock
- Added translations
- First DEV in a while (texures are missing but everything else should work. Also you NEED all dependencies otherwise it will crash (Will fix this in the future))


v2.49.203 BETA DEV
------------------

- Added custom achievement page
- Also allowed to customize the positions of the achievements


v2.49.160 BETA DEV
------------------

- Fixed #10


v2.49.159 BETA DEV
------------------

- Fixed #11


v2.49.157 BETA DEV
------------------

- Added Creative Tab- Creative Tab toggable in config- Moved DisplayUpdates in config
- Changed HashMaps to LinkedHashMaps so items and blocks stay in order.
- PulsatingBrainStone's particles will constantly change falling speed

v2.48.37 BETA release
---------------------

- New textures for the BrainStoneTrigger and the BrainLightSensor
- Fixed BrainStoneTrigger not rendering blocks mit meta data correctly
- Fixed Tools not working properly


v2.49.135 BETA DEV
------------------

- Fixed tools not working properly


v2.49.123 BETA DEV
------------------

- New textures


v2.49.119 BETA DEV
------------------

- Fixed BrainStoneTrigger not displaying correctly when using blocks with meta data
- Recommended build because everything is stable and some features are simply unfinished. You should be able to play with this version but you should be aware that this version might break something.


v2.49.115 BETA DEV
------------------

- Improved Jar verification


v2.49.109 BETA DEV
------------------

- Fixed the message not showing on the client
- Made sendToPlayer working again!


v2.49.78 BETA DEV
-----------------

- Various attempts to get the verification to work
- Failed verification will only display a very red message when the player joins a server or enters a world (This does not work because PlayerLoggingInEvent is not being fired clientside!)


v2.49.32 BETA DEV
-----------------

- Some improvements with the verification


v2.49.17 BETA DEV
-----------------

- Created first attemps to verify the jar. Testing right now
- Updated some URL's


v2.48.3 BETA release
--------------------

- Fixed several severe bugs


v2.48.1 BETA release
--------------------

- Update to 1.7.10
- Fixed a serverside crash in a very dirty way


v2.47.278 BETA DEV
------------------

- Improved some more sound and render stuff of the easter egg
- DEV-build! Test it!


v2.46.3 BETA release
--------------------

- Fixed a bug that sometimes causes the game to crash on start up.


v2.44.15 BETA release
---------------------

- Fixed tools (derp)


v2.42.1037 BETA prerelease
--------------------------

- Update to 1.7.2.
- Everything should be working but if you find any bugs please report them!


v2.42.821 BETA DEV
------------------

- Fixed the crashes while using the zip/jar instead of being in the working environment.


v2.42.809 BETA DEV
------------------

- Managed to get the Containers to work in a very hacky way... I hope this can be improved later
- Small improvement in BSP
- Please test this version


v2.42.671 BETA DEV
------------------

- Managed to get the TileEntities to work!
- Still having issues with Slots
- Fixed BrainLightSensor (including the smoking effect)
- Please test this build but DO NOT use it on your old worlds. It might corrupt them!


v2.42.512 BETA DEV
------------------



v2.42.462 BETA DEV
------------------

- Hard work on the network code (Finally sendig my own packets)
- Moved all the localizations to external files so they can be edited and added very easily (If you want your language here contact me.)
- Fixed the GUI-crash


v2.42.351 BETA DEV
------------------

- Fixed all network errors
- First attempts with the new networking
- Fixed all other errors
- Some small changes
- First development build (Please test!)


v2.43.37 BETA release
---------------------

- This build is required in order to update to 1.7.2
- You have to load every world you want to use in 1.7.2 with this mod at least one time with this build or all existing BrainStone Items and Blocks will disapear.


v2.37.1 BETA release
--------------------

- Fixed a critical bug that caused a game crash


v2.37.0 BETA release
--------------------

- Update to 1.6.4
- Changed the links for the version checker


v2.32.79 BETA release
---------------------

- Added logo


v2.32.50 BETA DEV
-----------------

- Fixed a bug that caused you beining kicked when a new version is available.
- Fixed a bug that a new recommended version was outputed as a release version


v2.32.35 BETA DEV
-----------------

- Changed many block textures


v2.30.63 BETA DEV
-----------------

- Added logo file. Logo is not displayed. This is a Forge bug!


v2.29.0 BETA release
--------------------

- Fixed the crash cased by the BLS (Issue #3)
- Everything is on the state of the last release (v2.26.46 BETA release)


v2.28.307 BETA DEV
------------------

- Pins now can be not connected (not 100% working)

===================================
This is just a DEV build to show off some new graphics and some new
handling. Please give me a short feedback on the GUI and the textures!


v2.26.46 BETA release
---------------------

- Changed the block register function (old one was deprecated)

====================================

THIS IS A RELEASE! ENJOY!!!


v2.26.43 BETA prerelease
------------------------

- This is the prerelease!
- Just one little bugfix
- Please test it and report any bug immediately!


v2.25.0 BETA DEV
----------------

- Completly added all function BSP
- Updated all calls of functions of BSP

=================================

This is the DEV-Build before the release! Please test it NOW!


v2.23.10 BETA DEV
-----------------

- Fixed Server Crash!
- Some minor improvements


v2.23.2 BETA DEV
----------------

- Update to 1.5.2


v2.22.15 BETA DEV
-----------------

- Fully implemented the new GUI of the BLS:
  - A new mode was added
  - The mode transforms the light strength into redstone signal strength
  - Or the other way round: Most light no power; No light most power
- Some cleaning up


v2.21.24 BETA DEV
-----------------

- Builds now are saved under /builds


v2.19.97 BETA release
---------------------

- New texture of the DirtyBrainStone
- In the treasury of the BrainStone dungeon (the hut) is located one PulsatingBrainStone under the floor. Caution!
- BrainStoneOre will drop more than before using Furtune Pickaxes
- Updated the German translation
- Update to 1.5.1
- Shift-click now works in the GUI of the BrainStoneTrigger


v2.18.123 BETA Pre-release
--------------------------

- Unknown


v2.17.15 BETA release
---------------------

- A surprise! (More information when you guess/find it!)
- PulsatingBrainStone added
- BrainStoneArmor added
- The camouflage mode of the BrainStoneTrigger is working (Need Add-Ons for other mods)
- Updated to 1.4.7 (compatible with 1.4.6)


v2.12.1 BETA release
--------------------

- Hotfix! (You now can put the .zip into the mods folder and it works)
- New Gui for the BrainStoneTrigger!
- The BrainStoneOre drops 10-20 EX Points.


v2.10.9 BETA release
--------------------



v1.48.27 BETA release
---------------------

- BrainLogicBlock added! (Gee! That was a huge piece of work!!!)
- BrainProcessor added
- New achievement added
- (Tool)Enchantments added
- all blocks added to the creativ inventory right behind the redstone lamp
- The grass bug at the BrainStoneTrigger fixed
- The update bug at the BrainStoneTrigger fixed (see next point)
- The BrainStoneTrigger now has a turn off delay of 5 ticks.
- You can now see the light level in the BrainLightSensor`s GUI (yellow frame)
- Uncountable small things and further bug fixes


v1.8.2 BETA release
-------------------

- In the GUI of the BrainStoneTrigger you now can set what kind of entity will be triggered. (You now can also hide it with grass (Buggy!))
- Achievements fixed
- More functions added to the GUI of the BrainLightSensor (arrow keys)


v1.6.46 BETA release
--------------------

- BrainStoneTrigger added
- Achievements added
- The BrainLightSensor got a new GUI. This GUI has some new functions
- Bugfixes!


v1.4.3 BETA release
-------------------

- The generation of BrainStoneOre finally works
- German translations
- The GUI of the BrainLightSensor works
- Every BrainLightSensor works now independently
- The pickaxe now works as a pickaxe and no longer as a shovel
- Changed the crafting recipe of the BrainLightSensor
- The CoalBriquette works now as a fuel
- From now also available as .7zip
