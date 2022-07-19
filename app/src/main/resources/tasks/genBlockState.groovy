// Load the block state template
println "Loading json."
def template = loadJson("${dir}/templates/templateBlockState.json")

// Update variables in the template
println "Updating json."
updateJsonVariables(template, variables)

// Save the modified template
println "Saving json."
saveJson(template, "${pathBlockStates}${section.getChildValue("path")}${section.getName()}.json")