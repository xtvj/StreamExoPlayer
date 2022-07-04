package github.xtvj.streamexoplayer.data

import android.text.Html
import github.xtvj.streamexoplayer.utils.log
import github.xtvj.streamexoplayer.utils.md5
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.net.URLDecoder
import java.util.Base64.getDecoder
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainRepository @Inject constructor() {


    suspend fun getUrl(room: String): String {
        return try {
            if (room.isNotBlank()) {
                getRealUrl(room)
            } else {
                ""
            }
        } catch (ex: Exception) {
            log(ex.toString())
            ""
        }
    }

    private suspend fun getRealUrl(room: String): String {
        val roomUrl = "https://m.huya.com/$room"
        val header = arrayOf(
            "Content-Type",
            "application/x-www-form-urlencoded",
            "User-Agent",
            "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) " +
                    " Chrome/75.0.3770.100 Mobile Safari/537.36 "
        )
        val headerBuild: Headers = Headers.headersOf(*header)
        val request: Request = Request.Builder().url(roomUrl).headers(headerBuild).build()
        val client = OkHttpClient()
        val response: Response = client.newCall(request).execute()
        val body = response.body!!.string()
        val match = Regex("\"liveLineUrl\":\"([\\s\\S]*?)\"")
        val url = match.find(body)?.value
        val list = url?.split("\"")?.findLast { it.length > 12 }
        val decodedUrl = String(getDecoder().decode(list?.toByteArray(Charsets.UTF_8)))
//            此方式已失效
//            val ib = decodedUrl.split("?")
//            val r = ib[0].split("/")
//            val s = r[r.size - 1].replace(Regex(".flv|.m3u8"), "")
//            val c = ib[1].split("&", ignoreCase = false, limit = 3)
//            val c1 = c.dropWhile {
//                it.isBlank()
//            }
//            val n = c1.flatMap { it.split(Regex("=|&")) }.dropWhile { it.isBlank() }
//            val fm = URLDecoder.decode(n[n.indexOf("fm") + 1], "utf-8")
//            val u = String(getDecoder().decode(fm.toByteArray(Charsets.UTF_8)))
//            val p = u.split("_")[0]
//            val temp = System.currentTimeMillis()
//            val tempint = (temp * 10000)
//            val f = tempint.toString()
//            val l = n[n.indexOf("wsTime") + 1]
//            val t = "0"
//            val h = StringBuilder()
//                .append(p).append("_")
//                .append(t).append("_")
//                .append(s).append("_")
//                .append(f).append("_")
//                .append(l)
//
//            val m = md5(h.toString())
//            val y = c1.last()
//            val urlTemp = "%s?wsSecret=%s&wsTime=%s&u=%s&seqid=%s&%s".format(ib[0], m, l, t, f, y)
//            return ("https:$urlTemp").replace("hls", "flv").replace("m3u8", "flv")

        val split = decodedUrl.split("?")
        val url_hls = split[0]
        val anti_code = split[1]
        val url_m3u8 = url_hls.replace("bd.hls", "al.flv")
        val url1 = url_m3u8.replace("m3u8", "flv")
        val stream_name = url1.split("/").last().replace(Regex(".flv|.m3u8"), "")
        val hls_url = "https:$url1"
        val srcAntiCode = Html.fromHtml(anti_code, Html.FROM_HTML_MODE_LEGACY)
        val c = srcAntiCode.split("&").dropWhile { it.isBlank() }
        val n = c.flatMap { it.split(Regex("=|&")) }.dropWhile { it.isBlank() }
        val fm = URLDecoder.decode(n[n.indexOf("fm") + 1], "utf-8")
        val u = String(getDecoder().decode(fm.toByteArray(Charsets.UTF_8)))
        val hash_prefix = u.split("_").first()
        val user_id = 1463993859134
        val uuid = if (n.indexOf("uuid") < 0) "" else n[n.indexOf("uuid") + 1]
        val ctype = if (n.indexOf("ctype") < 0) "" else n[n.indexOf("ctype") + 1]
        val txyp = if (n.indexOf("txyp") < 0) "" else n[n.indexOf("txyp") + 1]
        val fs = if (n.indexOf("fs") < 0) "" else n[n.indexOf("fs") + 1]
        val t = if (n.indexOf("t") < 0) "" else n[n.indexOf("t") + 1]
        val sphdDC = if (n.indexOf("sphdDC") < 0) "" else n[n.indexOf("sphdDC") + 1]
        val sphdcdn = if (n.indexOf("sphdcdn") < 0) "" else n[n.indexOf("sphdcdn") + 1]
        val sphd = if (n.indexOf("sphd") < 0) "" else n[n.indexOf("sphd") + 1]
        val ratio = if (n.indexOf("ratio") < 0) "2000" else n[n.indexOf("ratio") + 1]
        val mill = System.currentTimeMillis()
        val seqid = (mill + user_id).toString()
        val wsTime = (mill/1000 + 3600).toString(16).replace("0x", "")
        val hash0 = md5("$seqid|$ctype|$t")
        val hash1 = md5(hash_prefix + "_" + user_id + "_" + stream_name + "_" + hash0 + "_" + wsTime)
        return if (ctype.contains("mobile")) {
            "%s?wsSecret=%s&wsTime=%s&uuid=%s&uid=%s&seqid=%s&ratio=%s&txyp=%s&fs=%s&ctype=%s&ver=1&t=%s&sv=2107230339&sphdDC=%s&sphdcdn=%s&sphd=%s"
                .format(
                    hls_url,
                    hash1,
                    wsTime,
                    uuid,
                    user_id.toString(),
                    seqid,
                    ratio,
                    txyp,
                    fs,
                    ctype,
                    t,
                    sphdDC,
                    sphdcdn,
                    sphd
                )
        } else {
            "%s?wsSecret=%s&wsTime=%s&seqid=%s&ctype=%s&ver=1&txyp=%s&fs=%s&ratio=%s&u=%s&t=%s&sv=2107230339"
                .format(hls_url, hash1, wsTime, seqid, ctype, txyp, fs, ratio, user_id.toString(), t)
        }
    }

}