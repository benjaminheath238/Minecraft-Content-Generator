if (section.getChildValue("type").equals("block")) {
    genBlockModel()
    genBlockItemModel()
} else if (section.getChildValue("type").equals("item")) {
    genItemModel()
} else {
    println "This type does not have a model."
}

void genBlockModel() {
    new File("${pathModelsBlock}${section.getChildValue("path")}").mkdirs();
    // Load the block state template
    println "Loading json."
    def template = loadJson("${dir}/templates/templateBlockModel.json")

    // Update variables in the template
    println "Updating json."
    updateJsonVariables(template, variables)

    // Save the modified template
    println "Saving json."
    saveJson(template, "${pathModelsBlock}${section.getChildValue("path")}${section.getName()}.json")
}

void genBlockItemModel() {
    new File("${pathModelsItem}${section.getChildValue("path")}").mkdirs();
    // Load the block state template
    println "Loading json."
    def template = loadJson("${dir}/templates/templateBlockItemModel.json")

    // Update variables in the template
    println "Updating json."
    updateJsonVariables(template, variables)

    // Save the modified template
    println "Saving json."
    saveJson(template, "${pathModelsItem}${section.getChildValue("path")}${section.getName()}.json")
}

void genItemModel() {
    new File("${pathModelsItem}${section.getChildValue("path")}").mkdirs();
    // Load the block state template
    println "Loading json."
    def template = loadJson("${dir}/templates/templateItemModel.json")

    // Update variables in the template
    println "Updating json."
    updateJsonVariables(template, variables)

    // Save the modified template
    println "Saving json."
    saveJson(template, "${pathModelsItem}${section.getChildValue("path")}${section.getName()}.json")
}