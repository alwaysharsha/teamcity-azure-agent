/*
 * Copyright 2000-2021 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.clouds.azure.arm.web

import jetbrains.buildServer.clouds.azure.arm.connector.AzureApiConnector
import jetbrains.buildServer.clouds.azure.arm.connector.AzureApiConnectorFactory
import jetbrains.buildServer.clouds.azure.arm.throttler.AzureRequestThrottlerCache
import kotlinx.coroutines.coroutineScope
import org.jdom.Content
import org.jdom.Element
import javax.servlet.http.HttpServletRequest

/**
 * Handles images request.
 */
internal class ImagesHandler: ResourceHandler {
    override suspend fun handle(request: HttpServletRequest, context: ResourceHandlerContext) = coroutineScope {
        val region = request.getParameter("region")
        val images = context.apiConnector.getImages(region)

        val imagesElement = Element("images")
        for ((id, props) in images) {
            imagesElement.addContent(Element("image").apply {
                setAttribute("id", id)
                setAttribute("osType", props[1])
                setAttribute("isGalleryImage", props[2])
                text = props[0]
            })
        }

        imagesElement
    }
}
