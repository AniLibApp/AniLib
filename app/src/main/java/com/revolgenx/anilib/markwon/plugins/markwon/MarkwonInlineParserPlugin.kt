package com.revolgenx.anilib.markwon.plugins.markwon

import androidx.annotation.NonNull
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParser
import io.noties.markwon.inlineparser.MarkwonInlineParser.FactoryBuilder
import org.commonmark.parser.Parser


class MarkwonInlineParserPlugin internal constructor(@param:NonNull private val factoryBuilder: FactoryBuilder) :
    AbstractMarkwonPlugin() {
    interface BuilderConfigure<B : FactoryBuilder?> {
        fun configureBuilder(@NonNull factoryBuilder: B)
    }

    override fun configureParser(@NonNull builder: Parser.Builder) {
        builder.inlineParserFactory(factoryBuilder.build())
    }

    @NonNull
    fun factoryBuilder(): FactoryBuilder {
        return factoryBuilder
    }

    companion object {
        @NonNull
        fun create(): MarkwonInlineParserPlugin {
            return create(MarkwonInlineParser.factoryBuilder())
        }

        @NonNull
        fun create(@NonNull configure: BuilderConfigure<FactoryBuilder?>): MarkwonInlineParserPlugin {
            val factoryBuilder = MarkwonInlineParser.factoryBuilder()
            configure.configureBuilder(factoryBuilder)
            return MarkwonInlineParserPlugin(factoryBuilder)
        }

        @NonNull
        fun create(@NonNull factoryBuilder: FactoryBuilder): MarkwonInlineParserPlugin {
            return MarkwonInlineParserPlugin(factoryBuilder)
        }

        @NonNull
        fun <B : FactoryBuilder?> create(
            @NonNull factoryBuilder: B,
            @NonNull configure: BuilderConfigure<B>
        ): MarkwonInlineParserPlugin {
            configure.configureBuilder(factoryBuilder)
            return MarkwonInlineParserPlugin(factoryBuilder!!)
        }
    }

}