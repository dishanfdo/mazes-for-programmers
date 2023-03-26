import java.awt.image.RenderedImage
import java.io.File
import javax.imageio.ImageIO

fun <T> MutableCollection<T>.addIfNotNull(value: T?) {
    if (value != null) {
        add(value)
    }
}

fun RenderedImage.saveAsPng(path: String) {
    ImageIO.write(this, "png", File(path))
}