# Minecraft Content Generator

Minecraft Content Generator (MCCG) is a tool to generate resources for minecraft mods.

MCCG is a complete rework of the project Minecraft Data Generator (MCDG). MCCG has expanded on MCDG by removing the fixed and hard-coded functionality, such as how blockstates, block and item models, localistation and textures are generated, and replacing it with groovy scripts and json files.

---

## Installation

### Requirements

* [Neutron](https://github.com/benjaminheath238/Neutron/)
* [Gradle](https://gradle.org/install/)
* [Java 17](https://adoptium.net/en-GB/)

### Process

1. Download the source code
2. Navigate into the directory containing the `gradlew` file
3. Enter in a terminal/shell either `.\gradlew.bat build` or `./gradlew build` for Windows or Linux systems respectively
4. Collect the generated jar file from `app/build/libs/`

---

## How to Use

> The following is WIP, it can and may change at any time and is not guaranteed to be accurate

Create a content file (`[some name].ini`). Add to this file all the resources to generate. The structure of this file explained in detail can be found [here](https://github.com/benjaminheath238/Neutron/blob/master/docs/config.md#Parsing). The parser used is INI_WITH_ARRAYS_AND_MAPS. Each complex option represents a resource entry. Each entry can have the paramenters and tasks listed below.

### Paramenters

| Name    | Value                                                                   | Required |
|---------|-------------------------------------------------------------------------|----------|
| layer   | `true`, `false`                                                         | true     |
| layer_N | Binary name of layer (section.subsection.layer), 0 <= N <= 2^32-1       | false    |
| type    | `item`, `block`, `itemGroup`                                            | true     |
| path    | The path to the texture, without the name, from `textures/block\|item/` | true     |
| name    | The name to use in localisation                                         | false    |
| size    | `NxN` where 0 <= N <= 2^31-1                                            | false    |

### Tasks

| Name          | Applicable to Types |
|---------------|---------------------|
| genModel      | `item`, `block`     |
| genBlockState | `block`             |
| genLocale     | All                 |
| genTexture    | `item`, `block`     |

On first run MCCG will generate a file called `mccg.ini` in this file the mod's id and the path to the content file can be added.

Finally In the mods resources directory run the jar file from a terminal/shell with the command `java -jar mccg-[version here].jar` this will run all the tasks generating the resources.

## Advanced Usage

On first run MCCG will generate in the directory it was run from two directories one called `tasks` containing groovy scripts and one called `templates` containing json files. It will also generate two files called `mccg.ini` and `latest.log`.

The `tasks` directory contains groovy scripts these scripts can be registered in the `mccg.ini` file under the `Tasks` section. Tasks take the following parameters.

| Name | Value                                               | Required |
|------|-----------------------------------------------------|----------|
| is   | `com.bjmh.mccg.tasks.Task\|TaskJSON\|TaskPNG`       | true     |
| on   | `start`, `each`, `end`                              | true     |
| path | The path to this script, from the current directory | true     |

The `templates` directory contains json templates for use by `TaskJSON`. These templates include block states, block models, block item models and item models. A template can have variables in by adding `${var name}` where the variable comes from a paramenter in the users content file.

---

## Licensing
This software is licensed under the MIT license the definition of which can be found in the [LICENSE](LICENSE) file.

MCCG is open source software. Minecraft is a registered trademark of Mojang. MCCG is not an official Minecraft product. It is not approved by or associated with Mojang.
