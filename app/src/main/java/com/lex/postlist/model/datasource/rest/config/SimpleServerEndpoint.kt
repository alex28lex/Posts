package com.lex.postlist.model.datasource.rest.config

import com.lex.postlist.model.datasource.rest.constant.RestUrls


class SimpleServerEndpoint(
    host: String = RestUrls.HOST,
    path: String? = RestUrls.PATH,
    api: String? = RestUrls.API
) : BaseServerEndpoint(host, path, api)