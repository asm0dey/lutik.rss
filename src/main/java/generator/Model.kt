package generator

import jetbrains.exodus.entitystore.Entity
import kotlinx.dnq.*
import kotlinx.dnq.simple.uri
import java.util.*

class Channel(entity: Entity) : XdEntity(entity) {
    companion object : XdNaturalEntityType<Channel>()

    var title by xdRequiredStringProp()
    var description by xdRequiredStringProp()
    var pubDate by xdRequiredStringProp()
    var link by xdRequiredStringProp()
    var image by xdChild0_1(Image::channel)
    val items by xdChildren0_N(Episode::channel)
    val generator: String = "Lutik Generator"
}

class Image(entity: Entity) : XdEntity(entity) {
    companion object : XdNaturalEntityType<Image>()

    val channel: Channel by xdParent(Channel::image)
    var link by xdStringProp()
    var url by xdRequiredStringProp { uri() }
    var title by xdStringProp()
}

class Episode(entity: Entity) : XdEntity(entity) {
    companion object : XdNaturalEntityType<Episode>()

    var channel: Channel by xdParent(Channel::items)
    var title by xdRequiredStringProp()
    var link by xdRequiredStringProp { uri() }
    var description by xdStringProp()
    var pubDate by xdStringProp()
    var image by xdStringProp()
    var added by xdDateTimeProp()
    val guid = UUID.randomUUID().toString()
}

class Subscription(entity: Entity) : XdEntity(entity) {
    companion object : XdNaturalEntityType<Subscription>()

    var url by xdRequiredStringProp(unique = true) { uri() }
    var name by xdStringProp()
}

