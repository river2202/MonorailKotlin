package info.goldenriver.monorail.core

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

open class Reader {

    interface ReaderDelegate {
        fun matchRequest(request: Request?, interaction: Interaction) : Boolean
    }

    private val mapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    private var readDelegate: ReaderDelegate? = null
    var contract: Contract? = null


    constructor()

    constructor(monoRailJsonString: String?) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        if (monoRailJsonString != null) {
            parse(monoRailJsonString)
        }
    }

    private fun parse(monoRailJsonString: String) {
        contract = mapper.readValue(monoRailJsonString)

        val contractFile = contract ?: return

        val baseUrl = contractFile.consumer?.baseUrl

        for (interaction in contractFile.interactions) {
            interaction.updatePath(baseUrl)
        }
    }

    fun getResponse(request: Request?) : Response? {
        val request = request ?: return null
        val matchedInteractions = contract?.interactions?.filter { (readDelegate?.matchRequest(request, it) ?: false) || it.matchRequest(request) }
        val bestMatch = matchedInteractions?.firstOrNull { !it.consumed } ?: matchedInteractions?.lastOrNull()
        bestMatch?.consumed = true
        return bestMatch?.response
    }
}


