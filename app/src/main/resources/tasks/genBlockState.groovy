// Layers are not operated on. See genTexture
// Only blocks have block states
if (section.getChildValue('layer') == 'true' || !section.getChildValue('type') == 'block') { return }

// Create the path to this block state
new File("${pathBlockStates}/").mkdirs()

// Load the block state template
List<String> template = loadJson("${dir}/templates/templateBlockState.json")

// Modify the template with the standard variables
updateJsonVariables(template, variables)

// Save the template under the correct name
saveJson(template, "${pathBlockStates}/${section.getChildValue('id')}.json")
