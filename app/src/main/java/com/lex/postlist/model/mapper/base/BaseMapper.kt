package com.lex.postlist.model.mapper.base

abstract class BaseMapper<out MODEL, in ENTITY> {

    abstract fun mapTo(entity: ENTITY): MODEL

    fun mapTo(entities: Collection<ENTITY>?): List<MODEL> =
            entities?.map { mapTo(it) }?.toCollection(ArrayList(entities.size)).orEmpty()
}
