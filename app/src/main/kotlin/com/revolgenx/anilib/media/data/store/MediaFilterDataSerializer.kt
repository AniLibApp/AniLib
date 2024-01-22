package com.revolgenx.anilib.media.data.store

import com.revolgenx.anilib.common.data.store.BaseSerializer
import kotlinx.serialization.KSerializer


class MediaFilterDataSerializer(override val defaultValue: MediaFilterData = MediaFilterData()) :
    BaseSerializer<MediaFilterData>(defaultValue) {
    override fun serializer(): KSerializer<MediaFilterData> = MediaFilterData.serializer()
}

class MediaTagCollectionDataSerializer(override val defaultValue: MediaTagCollectionData = MediaTagCollectionData()) :
    BaseSerializer<MediaTagCollectionData>(defaultValue) {
    override fun serializer(): KSerializer<MediaTagCollectionData> = MediaTagCollectionData.serializer()
}

class GenreCollectionDataSerializer(override val defaultValue: GenreCollectionData = GenreCollectionData()) :
    BaseSerializer<GenreCollectionData>(defaultValue) {
    override fun serializer(): KSerializer<GenreCollectionData> = GenreCollectionData.serializer()
}



class ExternalLinkSourceCollectionDataSerializer(override val defaultValue: ExternalLinkSourceCollectionData) :
    BaseSerializer<ExternalLinkSourceCollectionData>(defaultValue) {
    override fun serializer(): KSerializer<ExternalLinkSourceCollectionData> = ExternalLinkSourceCollectionData.serializer()
}