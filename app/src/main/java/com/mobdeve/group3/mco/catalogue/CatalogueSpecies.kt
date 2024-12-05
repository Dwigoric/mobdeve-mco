package com.mobdeve.group3.mco.catalogue

class CatalogueSpecies(
    id: String,
    commonName: String,
    scientificName: String,
    imageId: Int
) {
    var id = id
        private set

    var commonName = commonName
        private set

    var scientificName = scientificName
        private set

    var imageId = imageId
        private set
}