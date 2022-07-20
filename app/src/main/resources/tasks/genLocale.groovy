// Layers are not operated on. See genTexture
if (section.getChildValue('layer') == 'true') { return }

// The name that this section will have
String name = (section.getChild('name') == null)
        ? idToName("${section.getChildValue('id')}")
        : section.getChildValue('name')

// Append this sections entry in the format "TYPE.MODID.ID": "NAME"
appendToJson("${pathLang}/en_us.json", "${section.getChildValue('type')}.${modid}.${section.getChildValue('id')}: ${name}")

// Converts an id to a name
// by replacing "_" with white space and capitalizing the next letter
String idToName(String id) {
    char[] chars = id.toCharArray()

    for (int i = 0; i < chars.length; i++) {
        if (i == 0) {
            chars[i] = Character.toUpperCase(chars[i])
        }

        if (chars[i] == '_') {
            chars[i] = ' '
            chars[i + 1] = Character.toUpperCase(chars[i + 1])
        }
    }

    return new String(chars)
}
