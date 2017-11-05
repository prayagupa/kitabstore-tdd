package com.beard

class Http {

    static String httpGet(String resourceLocator, def request) {
        println("Starting $request")
        def http = new URL(resourceLocator).openConnection() as HttpURLConnection
        //http.getInputStream().getText("UTF-8")
        http.getResponseCode()
    }

    static String httpPost(String resourceLocator, def requestId, String requestBody, Map<String, String> headers) {

        println("Firing request $requestId")

        HttpURLConnection http = new URL("${resourceLocator}").openConnection() as HttpURLConnection

        http.with {
            doOutput = true
            requestMethod = 'POST'

            headers.each { h ->
                addRequestProperty(h.key, h.value)
            }

            getOutputStream().write(requestBody.getBytes("UTF-8"))
        }

        http.getResponseCode()
    }

}
