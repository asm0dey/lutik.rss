package generator

import jetbrains.exodus.database.TransientEntityStore
import kotlinx.dnq.XdModel
import kotlinx.dnq.query.*
import kotlinx.dnq.store.container.StaticStoreContainer
import kotlinx.dnq.util.initMetaData
import org.joda.time.DateTime
import java.io.File

fun initXodus(): TransientEntityStore {
    XdModel.registerNodes(
        Channel, Image, Episode, Subscription
    )

    val databaseHome = File(System.getProperty("user.home"), ".lutik-store")

    return StaticStoreContainer
        .init(
            dbFolder = databaseHome,
            environmentName = "db"
        ).apply {
            initMetaData(XdModel.hierarchy, this)
        }
}

val store = initXodus()

object Repository {
    fun addShow(url: String, name: String? = null): Subscription =
        store.transactional {
            Subscription.new {
                this.url = url
                this.name = name
            }
        }


    fun removeShow(url: String? = null, name: String? = null) {
        require(!url.isNullOrBlank() || !name.isNullOrBlank())
        store.transactional {
            Subscription
                .filter { (it.url eq url) or (it.name eq name) }
                .firstOrNull()
                ?.delete()
        }
    }

    fun listShows(): Map<String, String?> = store.transactional(readonly = true) {
        Subscription.all().asSequence().map { it.url to it.name }.toMap()
    }

    fun urls() = store
        .transactional {
            Subscription
                .all()
                .asSequence()
                .map { it.url }
                .toList()
        }

    fun saveEpisodeIfNeeded(
        episodeFullName: String,
        link: String,
        description: String,
        imageUrl: String?
    ) {
        store.transactional {
            val episodeNotExists = Episode
                .filter { it.title eq episodeFullName }
                .isEmpty
            if (episodeNotExists) {
                val lutik = Channel.filter { it.title eq "Lutik" }.single()
                Episode
                    .new {
                        title = episodeFullName
                        channel = lutik
                        this.link = link
                        this.description = description
                        image = imageUrl
                        pubDate = pubDate()
                        added = DateTime.now()
                    }
            }
        }
    }
}
