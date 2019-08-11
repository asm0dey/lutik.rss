package generator

import org.jsoup.Jsoup

object Scanner {
    fun scan() {

        Repository.urls().forEach { url ->
            Jsoup.connect(url).get().run {
                val episodeFullName =
                    select("h1.film-card__title").text() +
                            ". " +
                            select("button.film-watching__episode:nth-child(4) > span:last-child").text()

                val link = select("div.film-watching__downloads-btn:nth-child(2) > a:nth-child(1)").attr("href")
                val description =
                    select("div.film-card__meta:nth-child(1) > div:nth-child(2) > div:nth-child(1)").text()
                val imageUrl = select("div.film-card__image:nth-child(1) > img:nth-child(1)").attr("src")
                Repository.saveEpisodeIfNeeded(episodeFullName, link, description,imageUrl)
            }
        }
    }
}