/*
 * Copyright 2011-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.apigenerator

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File

/**
 * Create coroutine-reactive API based on the templates.
 *
 * @author Mikhael Sokolov
 */
class CreateKotlinCoroutinesApi {

    @ParameterizedTest
    @MethodSource("arguments")
    fun createInterface(argument: String) {
        val targetName = argument.replace("Commands", "CoroutinesCommands")
        val templateFile = File(Constants.TEMPLATES, "io/lettuce/core/api/$argument.java")
        val targetPackage = when {
            "RedisSentinel" in argument -> "io.lettuce.core.sentinel.api.coroutines"
            else -> "io.lettuce.core.api.coroutines"
        }

        KotlinPoetCompilationUnitFactory(
            templateFile = templateFile,
            src = Constants.KOTLIN_SOURCES,
            targetPackage = targetPackage,
            targetName = targetName,
            commentReplacer = {
                it
                    .replace("\\$\\{intent}".toRegex(), "Coroutine executed commands")
                    .replace("@author [a-zA-Z ]+".toRegex(), "@author Mikhael Sokolov")
                    .replace("@since [a-zA-Z\\d.]+".toRegex()) { "@since ${it.value.substringAfter("@since").trim().toBigDecimalOrNull().let { it ?: "6.0".toBigDecimal() }.coerceAtLeast(6.0.toBigDecimal())}" }
                    .removeSuffix(" ")
                    .plus("@generated by ${javaClass.name}")
            }
        ).create()
    }

    companion object {
        @JvmStatic
        fun arguments() = Constants.TEMPLATE_NAMES.toList()
    }
}