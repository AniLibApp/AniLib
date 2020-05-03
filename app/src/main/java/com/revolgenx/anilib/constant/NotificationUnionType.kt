package com.revolgenx.anilib.constant


enum class NotificationUnionType {
   ACTIVITY_MESSAGE,

    /**
     * A user has replied to your activity
     */
    ACTIVITY_REPLY,

    /**
     * A user has followed you
     */
    FOLLOWING,

    /**
     * A user has mentioned you in their activity
     */
    ACTIVITY_MENTION,

    /**
     * A user has mentioned you in a forum comment
     */
    THREAD_COMMENT_MENTION,

    /**
     * A user has commented in one of your subscribed forum threads
     */
    THREAD_SUBSCRIBED,

    /**
     * A user has replied to your forum comment
     */
    THREAD_COMMENT_REPLY,

    /**
     * An anime you are currently watching has aired
     */
    AIRING,

    /**
     * A user has liked your activity
     */
    ACTIVITY_LIKE,

    /**
     * A user has liked your activity reply
     */
    ACTIVITY_REPLY_LIKE,

    /**
     * A user has liked your forum thread
     */
    THREAD_LIKE,

    /**
     * A user has liked your forum comment
     */
    THREAD_COMMENT_LIKE,

    /**
     * A user has replied to activity you have also replied to
     */
    ACTIVITY_REPLY_SUBSCRIBED,

    /**
     * A new anime or manga has been added to the site where its related media is on the user's list
     */
    RELATED_MEDIA_ADDITION,

}