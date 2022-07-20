// Layers are not operated on. See genTexture
if (section.getChildValue('layer') == 'true') { return }

// Handle blocks and items in the correct way
if (section.getChildValue('type') == 'block') {
    genBlockModel()
    genBlockItemModel()
} else if (section.getChildValue('type') == 'item') {
    genItemModel()
}

// The code below generates models using templates
// All are very similar so for more detail see genBlockState

void genBlockModel() {
    new File("${pathModelsBlock}${section.getChildValue('path')}").mkdirs()

    List<String> template = loadJson("${dir}/templates/templateBlockModel.json")

    updateJsonVariables(template, variables)

    saveJson(template, "${pathModelsBlock}${section.getChildValue('path')}${section.getChildVaue('id')}.json")
}

void genBlockItemModel() {
    new File("${pathModelsItem}${section.getChildValue('path')}").mkdirs()

    List<String> template = loadJson("${dir}/templates/templateBlockItemModel.json")

    updateJsonVariables(template, variables)

    saveJson(template, "${pathModelsItem}${section.getChildValue('path')}${section.getChildVaue('id')}.json")
}

void genItemModel() {
    new File("${pathModelsItem}${section.getChildValue('path')}").mkdirs()

    List<String> template = loadJson("${dir}/templates/templateItemModel.json")

    updateJsonVariables(template, variables)

    saveJson(template, "${pathModelsItem}${section.getChildValue('path')}${section.getChildVaue('id')}.json")
}
