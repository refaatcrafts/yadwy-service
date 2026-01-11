package yadwy.app.yadwyservice.sharedkernel.infrastructure.config

import org.postgresql.util.PGobject
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import tools.jackson.databind.json.JsonMapper
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized

@Configuration
class JdbcConfig(
    private val jsonMapper: JsonMapper
) : AbstractJdbcConfiguration() {

    override fun userConverters(): List<Any> {
        return listOf(
            LocalizedWritingConverter(jsonMapper),
            LocalizedReadingConverter(jsonMapper)
        )
    }
}

@WritingConverter
class LocalizedWritingConverter(
    private val jsonMapper: JsonMapper
) : Converter<Localized, PGobject> {

    override fun convert(source: Localized): PGobject {
        return PGobject().apply {
            type = "jsonb"
            value = jsonMapper.writeValueAsString(source)
        }
    }
}

@ReadingConverter
class LocalizedReadingConverter(
    private val jsonMapper: JsonMapper
) : Converter<PGobject, Localized> {

    override fun convert(source: PGobject): Localized {
        return jsonMapper.readValue(source.value, Localized::class.java)
    }
}
