package github.xtvj.streamexoplayer.data

import github.xtvj.streamexoplayer.utils.log
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.net.URLDecoder
import java.security.MessageDigest
import java.util.*
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
        val match = Regex("\"liveLineUrl\":\"([\\s\\S]*?)\",")
        val url = match.find(body)?.value
        val list = url?.split("\"")?.findLast { it.length > 12 }
        val decoder = Base64.getDecoder()
        val decodedUrl = String(decoder.decode(list?.toByteArray(Charsets.UTF_8)))
//        if (decodedUrl.isBlank()){
//            return null
//        }
        if (decodedUrl.contains("replay")) {
            return decodedUrl
        } else {
//            //hw.hls.huya.com/src/78941969-2559461593-10992803837303062528-2693342886-10057-A-0-1-imgplus.m3u8
//           ?ratio=2000
//           &wsSecret=4bfea730e2939d69832131335c4e747f
//           &wsTime=62bd5426
//           &fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D
//           &ctype=tars_mobile&txyp=o%3Anceic2%3B&fs=bgct
//           &
//           &sphdcdn=al_7-tx_3-js_3-ws_7-bd_2-hw_2
//           &sphdDC=huya&sphd=264_*-265_*
//           &t=103

//            i: //hw.hls.huya.com/src/78941969-2559461593-10992803837303062528-2693342886-10057-A-0-1-imgplus.m3u8
//            b: ratio=2000&wsSecret=4bfea730e2939d69832131335c4e747f&wsTime=62bd5426&fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype=tars_mobile&txyp=o%3Anceic2%3B&fs=bgct&&sphdcdn=al_7-tx_3-js_3-ws_7-bd_2-hw_2&sphdDC=huya&sphd=264_*-265_*&t=103
//            r:  ['', '', 'hw.hls.huya.com', 'src', '78941969-2559461593-10992803837303062528-2693342886-10057-A-0-1-imgplus.m3u8']
//            s: 78941969-2559461593-10992803837303062528-2693342886-10057-A-0-1-imgplus
//            c:  ['ratio=2000', 'wsSecret=4bfea730e2939d69832131335c4e747f', 'wsTime=62bd5426', 'fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype=tars_mobile&txyp=o%3Anceic2%3B&fs=bgct&&sphdcdn=al_7-tx_3-js_3-ws_7-bd_2-hw_2&sphdDC=huya&sphd=264_*-265_*&t=103']
//            c:  ['ratio=2000', 'wsSecret=4bfea730e2939d69832131335c4e747f', 'wsTime=62bd5426', 'fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype=tars_mobile&txyp=o%3Anceic2%3B&fs=bgct&&sphdcdn=al_7-tx_3-js_3-ws_7-bd_2-hw_2&sphdDC=huya&sphd=264_*-265_*&t=103']
//            n:  {'ratio': '2000', 'wsSecret': '4bfea730e2939d69832131335c4e747f', 'wsTime': '62bd5426', 'fm': 'RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype'}
//            fm:  RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw==&ctype
//            u:  DWq8BcJ3h6DJt6TY_$0_$1_$2_$3
//            p:  DWq8BcJ3h6DJt6TY
//            f:  16564886128028710
//            l:  62bd5426
//            t:  0
//            h:  DWq8BcJ3h6DJt6TY_0_78941969-2559461593-10992803837303062528-2693342886-10057-A-0-1-imgplus_16564886128028710_62bd5426
//            m:  786a7ed8590f41dbf33cc073e83215db
//            y:  fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype=tars_mobile&txyp=o%3Anceic2%3B&fs=bgct&&sphdcdn=al_7-tx_3-js_3-ws_7-bd_2-hw_2&sphdDC=huya&sphd=264_*-265_*&t=103
//            url:  //hw.hls.huya.com/src/78941969-2559461593-10992803837303062528-2693342886-10057-A-0-1-imgplus.m3u8?wsSecret=786a7ed8590f41dbf33cc073e83215db&wsTime=62bd5426&u=0&seqid=16564886128028710&fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype=tars_mobile&txyp=o%3Anceic2%3B&fs=bgct&&sphdcdn=al_7-tx_3-js_3-ws_7-bd_2-hw_2&sphdDC=huya&sphd=264_*-265_*&t=103
//            该直播间源地址为：
//            https://hw.flv.huya.com/src/78941969-2559461593-10992803837303062528-2693342886-10057-A-0-1-imgplus.flv?wsSecret=786a7ed8590f41dbf33cc073e83215db&wsTime=62bd5426&u=0&seqid=16564886128028710&fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype=tars_mobile&txyp=o%3Anceic2%3B&fs=bgct&&sphdcdn=al_7-tx_3-js_3-ws_7-bd_2-hw_2&sphdDC=huya&sphd=264_*-265_*&t=103
            val ib = decodedUrl.split("?")
            val r = ib[0].split("/")
            val s = r[r.size - 1].replace(Regex(".flv|.m3u8"), "")
            val c = ib[1].split("&", ignoreCase = false, limit = 3)
            val c1 = c.dropWhile {
                it.isBlank()
            }
            val n = c1.flatMap { it.split(Regex("=|&")) }.dropWhile { it.isBlank() }
            val fm = URLDecoder.decode(n[n.indexOf("fm") + 1], "utf-8")
            val u = String(getDecoder().decode(fm.toByteArray(Charsets.UTF_8)))
            val p = u.split("_")[0]
            val temp = System.currentTimeMillis()
            val tempint = (temp * 10000)
            val f = tempint.toString()
            val l = n[n.indexOf("wsTime") + 1]
            val t = "0"
            val h = StringBuilder()
                .append(p).append("_")
                .append(t).append("_")
                .append(s).append("_")
                .append(f).append("_")
                .append(l)

            val m = md5(h.toString())
            val y = c1.last()
            val urlTemp = "%s?wsSecret=%s&wsTime=%s&u=%s&seqid=%s&%s".format(ib[0], m, l, t, f, y)
            return ("https:$urlTemp").replace("hls", "flv").replace("m3u8", "flv")

//            def live(e):
//            i, b = e.split('?')
//            r = i.split('/')
//            s = re.sub(r'.(flv|m3u8)', '', r[-1])
//            c = b.split('&', 3)
//            c = [i for i in c if i != '']
//            n = {i.split('=')[0]: i.split('=')[1] for i in c}
//            fm = urllib.parse.unquote(n['fm'])
//            u = base64.b64decode(fm).decode('utf-8')
//            p = u.split('_')[0]
//            f = str(int(time.time() * 1e7))
//            l = n['wsTime']
//            t = '0'
//            h = '_'.join([p, t, s, f, l])
//            m = hashlib.md5(h.encode('utf-8')).hexdigest()
//            y = c[-1]
//            url = "{}?wsSecret={}&wsTime={}&u={}&seqid={}&{}".format(i, m, l, t, f, y)
//            return url
        }
    }


    /**
     * md5加密字符串
     * md5使用后转成16进制变成32个字节
     */
    private fun md5(str: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val result = digest.digest(str.toByteArray())
        //没转16进制之前是16位
        println("result${result.size}")
        //转成16进制后是32字节
        return toHex(result)
    }

    private fun toHex(byteArray: ByteArray): String {
        val result = with(StringBuilder()) {
            byteArray.forEach {
                val hex = it.toInt() and (0xFF)
                val hexStr = Integer.toHexString(hex)
                if (hexStr.length == 1) {
                    this.append("0").append(hexStr)
                } else {
                    this.append(hexStr)
                }
            }
            this.toString()
        }
        //转成16进制后是32字节
        return result
    }

}