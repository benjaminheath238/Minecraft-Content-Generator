if (section.getChildValue("layer").equals("true")) return;

def name = (section.getChild("name") == null)
        ? idToName("${section.getChildValue("id")}")
        : section.getChildValue("name")

appendToJson("${pathLang}/en_us.json", "${section.getChildValue("type")}.${modid}.${section.getChildValue("id")}: ${name}");

String idToName(String id) {
    char[] chars = id.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (i == 0)
                chars[i] = Character.toUpperCase(chars[i]);

            if (chars[i] == '_') {
                chars[i] = ' ';
                chars[i + 1] = Character.toUpperCase(chars[i + 1]);
            }
        }

        return new String(chars);
}