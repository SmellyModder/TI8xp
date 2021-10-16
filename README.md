[![](https://jitpack.io/v/SmellyModder/TI8xp.svg)](https://jitpack.io/#SmellyModder/TI8xp)
# 📖 About
**TI8xp** is a small Java library for compiling and decompiling `.8xp` program files used on Texas Instrument Calculators (TI83/TI84/TI84+/TI84s).

This library is applicable for automating the creation of `.8xp` program files and analyzing the contents of `.8xp` program files.
The `.8xp` file format gets used by Texas Instrument Calculators to run custom programs. The possibilities of these programs are virtually endless, being used to replicate even entire games.

# 📦 Installing
**TI8xp** uses Jitpack to easily publish the library.
## Gradle
* Add the maven in your `repositories` section:
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
* Add it as a dependency in your `dependencies` section:
```gradle
dependencies {
    implementation 'com.github.SmellyModder:TI8xp:{version}'
}
```
## Maven
* Add the repository to your build file
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
* Add the dependency
```xml
<dependency>
    <groupId>com.github.SmellyModder</groupId>
    <artifactId>TI8xp</artifactId>
    <version>VERSION</version>
</dependency>
```
