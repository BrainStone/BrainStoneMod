[Brain Stone Mod](https://minecraft.curseforge.com/projects/brain-stone-mod)
============================================================================

[![Curse Forge](http://cf.way2muchnoise.eu/short_250836_downloads.svg)](https://minecraft.curseforge.com/projects/brain-stone-mod)
[![Build Status](https://gitlab.crazyblock-network.net/BrainStone/brainstone/badges/master/build.svg)](https://gitlab.crazyblock-network.net/BrainStone/brainstone/commits/master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5c4ec0299d094cf3a46a3104122bcc76)](https://www.codacy.com/app/BrainStone/brainstone?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=BrainStone/brainstone&amp;utm_campaign=Badge_Grade)

Starts where all others end

Issue reporting
---------------

Please include the following:

* Minecraft version
* Brain Stone Mod version
* Forge version/build
* Versions of any mods potentially related to the issue 
* Any relevant screenshots are greatly appreciated.
* For crashes:
	* Steps to reproduce
	* ForgeModLoader-client-0.log (the FML log) from the root folder of the client
 
*(When creating a new issue please follow the template)*	

Developing with my mod
----------------------

So you want to use items or blocks from my mod, add support or even develop an addon for my mod then you can easily add it to your development environment! All
releases beginning from version 1.10.2-4.0.19 get uploaded to my maven repository.  
So all you have to do to include the mod is add these lines *(in the appropriate places)* to your build.gradle

    repositories {
        maven { // BrainStoneMod
            url "http://maven.brainstonemod.com"
        }
        // Other repos...
    }
    
    dependencies {
        compile "brainstonemod:BrainStoneMod:<version>:deobf"
        // Other dependencies
    }

Setting up a Workspace/Compiling from Source
--------------------------------------------

* Setup: Run [gradle] in the repository root: `gradlew[.bat] [setupDevWorkspace|setupDecompWorkspace] installLombok [eclipse|idea]`
* Build: Run [gradle] in the repository root: `gradlew[.bat] build`
* If obscure Gradle issues are found try running `gradlew clean` and `gradlew cleanCache`

Licenses
--------

Code, Textures and binaries are licensed under the [GPLv3](https://www.gnu.org/licenses/#GPL).

You are allowed to use the mod in your modpack.  
Any modpack which uses Brain Stone Mod takes **full** responsibility for user support queries. For anyone else, we only support official builds from the main
download server, not custom built jars. We do take bug reports for outdated builds of Minecraft. However we cannot promise to fix any issues. We will try
regardless.

Any alternate licenses are noted where appropriate.

Jar Signing
-----------

All jars from all official download sources will be signed begining from version v2.53.115 BETA DEV. The signature will always have a SHA-1 hash of
`2238d4a92d81ab407741a2fdb741cebddfeacba6` and you are free to verify it.
