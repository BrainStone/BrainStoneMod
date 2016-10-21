# Set variables in and from mcmod.info
sed -i -e s/BUILDNUMBER/${BUILD_NUMBER}/g ${WORKSPACE}/src/main/resources/mcmod.info
cp -p ${WORKSPACE}/src/main/resources/mcmod.info /home/thefireplace/adobeblocks/modversion.info
sed -i -e'/"version"/!d' /home/thefireplace/adobeblocks/modversion.info
sed -i -e 's/  "version": "//g' /home/thefireplace/adobeblocks/modversion.info
sed -i -e 's/",//g' /home/thefireplace/adobeblocks/modversion.info
VERSION="$(sed '/AAAA/d' /home/thefireplace/adobeblocks/modversion.info)"
# Set up the update json
cp -p ${WORKSPACE}/update.json /opt/bitnami/apache2/htdocs/jsons/adobeblocks.json
sed -i -e s/!!VERSION!!/$VERSION/g  /opt/bitnami/apache2/htdocs/jsons/adobeblocks.json
# Begin work on the buildscript and build itself
rm ${WORKSPACE}/gradlew
cp -p /home/thefireplace/gradlew ${WORKSPACE}/gradlew
cd ${WORKSPACE}/
sed -i -e s/!!VERSION!!/$VERSION/g  build.gradle
cd ../builds/${BUILD_NUMBER}/
sed -n -e '/    /w changelog.txt' changelog.xml
cp -p changelog.txt /opt/bitnami/apache2/htdocs/changelog.txt
cp -p changelog.txt ${WORKSPACE}/changelog.txt
cd ${WORKSPACE}/
grep "release version" -q changelog.txt && sed -i -e "s/releaseType = 'beta'/releaseType = 'release'/g"  build.gradle
unset JAVA_OPTS
bash gradlew -P curseForgeApiKey=omitted -P modsioApiKey=omitted build uploadToModsIO curseforge jar
# Cleanup
rm /home/thefireplace/adobeblocks/deobf/*.jar
cp ${WORKSPACE}/build/libs/*.jar /home/thefireplace/adobeblocks/deobf/
rm changelog.txt
rm /home/thefireplace/adobeblocks/modversion.info
rm ${WORKSPACE}/build/libs/*.jar