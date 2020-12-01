package com.kangtaejong98.tuview.notification

import org.jsoup.nodes.Document
import java.io.Serializable

data class NotificationData(
        var title: String = "",
        var author: String = ""
) : Serializable {
    companion object {
        fun build(document: Document): NotificationData {
            val title = getTitle(document)
            val author = getAuthor(document)

            return NotificationData(title, author)
        }

        private fun getTitle(document: Document): String {
            return try {
                var title = ""
                val elements = document.getElementsByTag("meta")
                for (element in elements) {
                    if (element.attr("name") == "title") {
                        title = element.attr("content")
                        break
                    }
                }

                title
            } catch (e: Exception) {
                "Title"
            }
        }

        private fun getAuthor(document: Document): String {
            return try {
                var author = ""
                val elements = document.getElementsByTag("link")
                for (element in elements) {
                    if (element.attr("itemprop") == "name") {
                        author = element.attr("content")
                    }
                }

                author
            } catch (e: Exception) {
                "Author"
            }
        }
    }
}