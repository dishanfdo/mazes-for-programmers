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

/**
 * Remove all elements comes after the given index
 */
fun <T> MutableList<T>.removeAllAfter(index: Int) {
    removeAll(slice((index + 1) until size))
}

fun <T> MutableCollection<T>.removeRandomElement(): T {
    val element = random()
    remove(element)
    return element
}

@Suppress("NOTHING_TO_INLINE")
inline fun Int.isEven() = this % 2 == 0