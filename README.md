# CraftableEnchants

A Bukkit/Spigot plugin that gives certain items built-in enchantments and adds new enchantments.

![CraftableEnchants project cover](.github/readme-cover.svg)

## Table of Contents

- [Build](#build)
- [Technologies](#technologies)

## Build

The Maven project targets Bukkit 1.13.2. From the repository root:

```sh
mvn package
```

The build is configured to write the plugin JAR to `target/`. Compatibility with current Minecraft server versions has not been verified.

## Technologies

- [Bukkit](https://hub.spigotmc.org/javadocs/bukkit/) provides the server plugin API.
- [Java](https://dev.java/) and [Maven](https://maven.apache.org/) are used to build the plugin.
