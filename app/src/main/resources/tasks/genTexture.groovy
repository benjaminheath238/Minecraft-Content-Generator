// Import config classes
import com.bjmh.lib.io.config.ConfigPath
import com.bjmh.lib.io.config.ConfigSection

// Layers are not operated on
if (section.getChild("layer") != null && section.getChildValue("layer").equals("true")) return

// This is the base image. The default size is 16x16 pixels
def base = section.getChild("size") != null ? newImage(section.getChildValue("size"))
                                            : newImage("16x16")

// Iterate through all the layers
for (int i = 0; true; i++) {
    // Stops at the first null layer
    if (section.getChild("layer_${i}") == null)
        break;

    // Gets the config node this layer is set to
    def node = com.bjmh.mccg.Main.CONTENT_CONFIG.getChild(new ConfigPath(section.getChildValue("layer_${i}")))

    if (!(node instanceof ConfigSection))
        break;

    def layer = (ConfigSection) node

    def image = null

    // Looks for this image in different locations dependent on type
    if (section.getChildValue("type").equals("block")) {
        image = loadImage("${pathTexturesBlock}${layer.getChildValue("path")}${layer.getChildValue("id")}.png")
    } else if (section.getChildValue("type").equals("item")){
        image = loadImage("${pathTexturesItem}${layer.getChildValue("path")}${layer.getChildValue("id")}.png")
    }

    // Write this layer to the base image
    writeLayer(base, image, "${layer.getChildValue("transparent")}")
}

// Saves this image
if (section.getChildValue("type").equals("block")) {
    saveImage(base, "${pathTexturesBlock}${section.getChildValue("path")}${section.getChildValue("id")}.png")
} else if (section.getChildValue("type").equals("item")){
    saveImage(base, "${pathTexturesItem}${section.getChildValue("path")}${section.getChildValue("id")}.png")
}