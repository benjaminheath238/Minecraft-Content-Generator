if (section.getChildValue("layer").equals("true")) return;

if (section.getChildValue("type").equals("block")) {
    genBlockModel()
    genBlockItemModel()
} else if (section.getChildValue("type").equals("item")) {
    genItemModel()
}

void genBlockModel() {
    new File("${pathModelsBlock}${section.getChildValue("path")}").mkdirs()
    
    def template = loadJson("${dir}/templates/templateBlockModel.json")

    updateJsonVariables(template, variables)

    saveJson(template, "${pathModelsBlock}${section.getChildValue("path")}${section.getName()}.json")
}

void genBlockItemModel() {
    new File("${pathModelsItem}${section.getChildValue("path")}").mkdirs()
    
    def template = loadJson("${dir}/templates/templateBlockItemModel.json")

    updateJsonVariables(template, variables)

    saveJson(template, "${pathModelsItem}${section.getChildValue("path")}${section.getName()}.json")
}

void genItemModel() {
    new File("${pathModelsItem}${section.getChildValue("path")}").mkdirs()
    
    def template = loadJson("${dir}/templates/templateItemModel.json")

    updateJsonVariables(template, variables)

    saveJson(template, "${pathModelsItem}${section.getChildValue("path")}${section.getName()}.json")
}