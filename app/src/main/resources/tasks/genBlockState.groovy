if (section.getChildValue("layer").equals("true") || !section.getChildValue("type").equals("block")) return;

new File("${pathBlockStates}${section.getChildValue("path")}").mkdirs();

def template = loadJson("${dir}/templates/templateBlockState.json");

updateJsonVariables(template, variables)

saveJson(template, "${pathBlockStates}${section.getChildValue("path")}${section.getName()}.json")