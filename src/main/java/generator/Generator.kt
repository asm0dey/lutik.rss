package generator

import kotlinx.dnq.query.asSequence
import kotlinx.dnq.query.filter
import kotlinx.dnq.query.sortedBy
import org.redundent.kotlin.xml.XmlVersion
import org.redundent.kotlin.xml.xml

object Generator {
    fun geenrateRss(prettyPrint: Boolean): String {
        return xml("rss", encoding = "UTF-8", version = XmlVersion.V10) {
            attributes(
                "version" to "2.0",
                "xmlns:media" to "http://search.yahoo.com/mrss/",
                "xmlns:atom" to "http://www.w3.org/2005/Atom"
            )
            store.transactional(readonly = true) {
                Channel.filter { it.title eq "Lutik" }.asSequence().forEach {
                    "channel"{
                        "atom:link"{
                            attributes(
                                "href" to "http://dallas.example.com/rss.xml",
                                "rel" to "self",
                                "type" to "application/rss+xml"
                            )
                        }
                        "title"{ -it.title }
                        "link"{ -it.link }
                        it.description.let { "description"{ cdata(it) } }
                        "pubDate"{ -it.pubDate }
                        "generator"{ -it.generator }
                        it.items
                            .sortedBy(Episode::added, asc = false)
                            .asSequence()
                            .take(50)
                            .forEach {
                                "item"{
                                    "title"{ -it.title }
                                    "link"{ -it.link }
                                    it.description?.let { "description"{ cdata(it) } }
                                    it.pubDate?.let { "pubDate"{ -it } }
                                    it.image?.let {
                                        "media:content"{
                                            attributes("medium" to "image", "url" to it)
                                        }
                                    }
                                    "guid"{
                                        attribute("isPermaLink", false)
                                        -it.guid
                                    }
                                }
                            }
                    }
                }

            }
        }
            .toString(prettyPrint)

    }
}
