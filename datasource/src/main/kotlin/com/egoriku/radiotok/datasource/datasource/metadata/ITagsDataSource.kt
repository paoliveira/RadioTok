package com.egoriku.radiotok.datasource.datasource.metadata

import com.egoriku.radiotok.datasource.entity.MetadataEntity

interface ITagsDataSource {

    suspend fun load(): List<MetadataEntity>
}