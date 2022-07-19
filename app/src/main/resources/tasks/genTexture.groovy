import com.bjmh.lib.io.config.ConfigPath
import com.bjmh.lib.io.config.ConfigSection

if (section.getChild("layer") != null && section.getChildValue("layer").equals("true")) return

def base = section.getChild("size") != null ? newImage(section.getChildValue("size"))
                                            : newImage("16x16")

for (int i = 0; true; i++) {
    if (section.getChild("layer_${i}") == null)
        break;

    def node = com.bjmh.mccg.Main.CONTENT_CONFIG.getChild(new ConfigPath(section.getChildValue("layer_${i}")))

    if (!(node instanceof ConfigSection))
        break;

    def layer = (ConfigSection) node

    def image = null

    if (section.getChildValue("type").equals("block")) {
        image = loadImage("${pathTexturesBlock}${layer.getChildValue("path")}${layer.getChildValue("id")}.png")
    } else if (section.getChildValue("type").equals("item")){
        image = loadImage("${pathTexturesItem}${layer.getChildValue("path")}${layer.getChildValue("id")}.png")
    }

    writeLayer(base, image, "${layer.getChildValue("transparent")}")
}

if (section.getChildValue("type").equals("block")) {
    saveImage(base, "${pathTexturesBlock}${section.getChildValue("path")}${section.getChildValue("id")}.png")
} else if (section.getChildValue("type").equals("item")){
    saveImage(base, "${pathTexturesItem}${section.getChildValue("path")}${section.getChildValue("id")}.png")
}