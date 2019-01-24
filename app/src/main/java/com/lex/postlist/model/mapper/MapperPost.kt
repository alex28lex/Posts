package com.lex.postlist.model.mapper


import com.lex.postlist.model.entity.PostEntity
import com.lex.postlist.model.mapper.base.BaseMapper
import com.lex.postlist.model.viewobject.PostVo


class MapperPost : BaseMapper<PostVo, PostEntity>() {

    override fun mapTo(entity: PostEntity): PostVo {
        return PostVo(entity.title, entity.body)
    }
}