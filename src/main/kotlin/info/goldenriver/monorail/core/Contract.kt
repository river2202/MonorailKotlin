package info.goldenriver.monorail.core

data class Contract (
    var consumer: ConsumerVariables, var interactions: List<Interaction>)

data class ConsumerVariables (
    val baseUrl: String)

data class Interaction (
    var id: String? = null,
    var request: Request,
    val response: Response? = null
) {
    var consumed = false

    fun matchRequest(req: Request) : Boolean {
        val method = request.method
        val path = request.path

        if (method == null || path == null ) {
            return false
        }
        return req.method == method && req.path?.endsWith(path, true)
    }

    fun updatePath(baseUrl: String) {
        request?.path = request?.path.removePrefix(baseUrl)
    }

}

data class Request (val method: String,
                    var path: String,
                    val headers: Map<String, String> = emptyMap(),
                    val body: Any? = null,
                    val data: String? = null
)

data class Response (
    val status: Int,
    val headers: Map<String, String> = emptyMap(),
    val body: Any? = null,
    val data: String? = null
)
