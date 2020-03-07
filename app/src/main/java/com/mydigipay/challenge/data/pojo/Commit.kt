package com.mydigipay.challenge.data.pojo

import com.google.gson.annotations.SerializedName

data class CommitDetail(
    @SerializedName("sha")
    val sha: String,
    @SerializedName("node_id")
    val nodeId: String,
    @SerializedName("commit")
    val commit: Commit,
    @SerializedName("committer")
    val committer: Owner
)

data class Commit(
    @SerializedName("message")
    val message: String
)