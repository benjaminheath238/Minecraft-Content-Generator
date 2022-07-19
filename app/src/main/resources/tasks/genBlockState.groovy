// Stop if the section is not a block
if (!section.getChildValue("type").equals("block")) return;

new File("${pathBlockStates}${section.getChildValue("path")}").mkdirs();

// Load the block state template
println "Loading json."
def template = loadJson("${dir}/templates/templateBlockState.json");

// Update variables in the template
println "Updating json."
updateJsonVariables(template, variables)

// Save the modified template
println "Saving json."
saveJson(template, "${pathBlockStates}${section.getChildValue("path")}${section.getName()}.json")