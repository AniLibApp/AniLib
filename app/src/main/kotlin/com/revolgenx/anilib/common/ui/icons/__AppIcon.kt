package com.revolgenx.anilib.common.ui.icons

import IcAiring
import IcOpenInNew
import IcShare
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.appicon.IcAbout
import com.revolgenx.anilib.common.ui.icons.appicon.IcAnilib
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowDown
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowDownward
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowUp
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowUpward
import com.revolgenx.anilib.common.ui.icons.appicon.IcAssignment
import com.revolgenx.anilib.common.ui.icons.appicon.IcAutorenew
import com.revolgenx.anilib.common.ui.icons.appicon.IcBack
import com.revolgenx.anilib.common.ui.icons.appicon.IcBold
import com.revolgenx.anilib.common.ui.icons.appicon.IcBook
import com.revolgenx.anilib.common.ui.icons.appicon.IcBookOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcBookmark
import com.revolgenx.anilib.common.ui.icons.appicon.IcBugReport
import com.revolgenx.anilib.common.ui.icons.appicon.IcCalculate
import com.revolgenx.anilib.common.ui.icons.appicon.IcCalendar
import com.revolgenx.anilib.common.ui.icons.appicon.IcCancel
import com.revolgenx.anilib.common.ui.icons.appicon.IcCenter
import com.revolgenx.anilib.common.ui.icons.appicon.IcChart
import com.revolgenx.anilib.common.ui.icons.appicon.IcCheck
import com.revolgenx.anilib.common.ui.icons.appicon.IcChevronLeft
import com.revolgenx.anilib.common.ui.icons.appicon.IcChevronRight
import com.revolgenx.anilib.common.ui.icons.appicon.IcClose
import com.revolgenx.anilib.common.ui.icons.appicon.IcCode
import com.revolgenx.anilib.common.ui.icons.appicon.IcCreate
import com.revolgenx.anilib.common.ui.icons.appicon.IcDelete
import com.revolgenx.anilib.common.ui.icons.appicon.IcDropped
import com.revolgenx.anilib.common.ui.icons.appicon.IcErrorAnilib
import com.revolgenx.anilib.common.ui.icons.appicon.IcExplore
import com.revolgenx.anilib.common.ui.icons.appicon.IcFall
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcFire
import com.revolgenx.anilib.common.ui.icons.appicon.IcForum
import com.revolgenx.anilib.common.ui.icons.appicon.IcForumOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcGithub
import com.revolgenx.anilib.common.ui.icons.appicon.IcGroup
import com.revolgenx.anilib.common.ui.icons.appicon.IcGroupOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcHappy
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeader
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeart
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeartOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcHide
import com.revolgenx.anilib.common.ui.icons.appicon.IcHistory
import com.revolgenx.anilib.common.ui.icons.appicon.IcHome
import com.revolgenx.anilib.common.ui.icons.appicon.IcHomeOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcHourglass
import com.revolgenx.anilib.common.ui.icons.appicon.IcImage
import com.revolgenx.anilib.common.ui.icons.appicon.IcInfo
import com.revolgenx.anilib.common.ui.icons.appicon.IcInfoOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcItalics
import com.revolgenx.anilib.common.ui.icons.appicon.IcLayoutStyle
import com.revolgenx.anilib.common.ui.icons.appicon.IcLibraryBooks
import com.revolgenx.anilib.common.ui.icons.appicon.IcLink
import com.revolgenx.anilib.common.ui.icons.appicon.IcList
import com.revolgenx.anilib.common.ui.icons.appicon.IcLogin
import com.revolgenx.anilib.common.ui.icons.appicon.IcLogout
import com.revolgenx.anilib.common.ui.icons.appicon.IcMainInline
import com.revolgenx.anilib.common.ui.icons.appicon.IcMarkdownGif
import com.revolgenx.anilib.common.ui.icons.appicon.IcMarkdownMediaPlay
import com.revolgenx.anilib.common.ui.icons.appicon.IcMarkdownYoutube
import com.revolgenx.anilib.common.ui.icons.appicon.IcMedia
import com.revolgenx.anilib.common.ui.icons.appicon.IcMediaOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcMessage
import com.revolgenx.anilib.common.ui.icons.appicon.IcMinus
import com.revolgenx.anilib.common.ui.icons.appicon.IcMoreHoriz
import com.revolgenx.anilib.common.ui.icons.appicon.IcMoreVert
import com.revolgenx.anilib.common.ui.icons.appicon.IcNeutral
import com.revolgenx.anilib.common.ui.icons.appicon.IcNotification
import com.revolgenx.anilib.common.ui.icons.appicon.IcNotificationOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcOrderedList
import com.revolgenx.anilib.common.ui.icons.appicon.IcPalette
import com.revolgenx.anilib.common.ui.icons.appicon.IcPaused
import com.revolgenx.anilib.common.ui.icons.appicon.IcPencil
import com.revolgenx.anilib.common.ui.icons.appicon.IcPercent
import com.revolgenx.anilib.common.ui.icons.appicon.IcPerson
import com.revolgenx.anilib.common.ui.icons.appicon.IcPersonAdd
import com.revolgenx.anilib.common.ui.icons.appicon.IcPersonCheck
import com.revolgenx.anilib.common.ui.icons.appicon.IcPersonCheckOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcPersonOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcPin
import com.revolgenx.anilib.common.ui.icons.appicon.IcPlanning
import com.revolgenx.anilib.common.ui.icons.appicon.IcPlay
import com.revolgenx.anilib.common.ui.icons.appicon.IcPlus
import com.revolgenx.anilib.common.ui.icons.appicon.IcPrivacyPolicy
import com.revolgenx.anilib.common.ui.icons.appicon.IcPublic
import com.revolgenx.anilib.common.ui.icons.appicon.IcQuestionMark
import com.revolgenx.anilib.common.ui.icons.appicon.IcQuote
import com.revolgenx.anilib.common.ui.icons.appicon.IcRecommendation
import com.revolgenx.anilib.common.ui.icons.appicon.IcReply
import com.revolgenx.anilib.common.ui.icons.appicon.IcReview
import com.revolgenx.anilib.common.ui.icons.appicon.IcSad
import com.revolgenx.anilib.common.ui.icons.appicon.IcSadAnilib
import com.revolgenx.anilib.common.ui.icons.appicon.IcSave
import com.revolgenx.anilib.common.ui.icons.appicon.IcSearch
import com.revolgenx.anilib.common.ui.icons.appicon.IcSettings
import com.revolgenx.anilib.common.ui.icons.appicon.IcSpoiler
import com.revolgenx.anilib.common.ui.icons.appicon.IcSpring
import com.revolgenx.anilib.common.ui.icons.appicon.IcStar
import com.revolgenx.anilib.common.ui.icons.appicon.IcStarOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcStats
import com.revolgenx.anilib.common.ui.icons.appicon.IcStrikeThrough
import com.revolgenx.anilib.common.ui.icons.appicon.IcStudio
import com.revolgenx.anilib.common.ui.icons.appicon.IcSummer
import com.revolgenx.anilib.common.ui.icons.appicon.IcThumbDown
import com.revolgenx.anilib.common.ui.icons.appicon.IcThumbUp
import com.revolgenx.anilib.common.ui.icons.appicon.IcTime
import com.revolgenx.anilib.common.ui.icons.appicon.IcTranslate
import com.revolgenx.anilib.common.ui.icons.appicon.IcTune
import com.revolgenx.anilib.common.ui.icons.appicon.IcTv
import com.revolgenx.anilib.common.ui.icons.appicon.IcUnfold
import com.revolgenx.anilib.common.ui.icons.appicon.IcVideo
import com.revolgenx.anilib.common.ui.icons.appicon.IcVoice
import com.revolgenx.anilib.common.ui.icons.appicon.IcWatch
import com.revolgenx.anilib.common.ui.icons.appicon.IcWatching
import com.revolgenx.anilib.common.ui.icons.appicon.IcWidgets
import com.revolgenx.anilib.common.ui.icons.appicon.IcWinter
import com.revolgenx.anilib.common.ui.icons.appicon.IcYoutube

object AppIcons

private var __AllIcons: List<ImageVector>? = null

val AppIcons.AllIcons: List<ImageVector>
    get() {
        if (__AllIcons != null) {
            return __AllIcons!!
        }
        __AllIcons = listOf(
            IcAbout,
            IcAiring,
            IcShare,
            IcOpenInNew,
            IcAnilib,
            IcArrowDown,
            IcArrowUp,
            IcAutorenew,
            IcBack,
            IcBook,
            IcBookmark,
            IcBookOutline,
            IcCalculate,
            IcCalendar,
            IcCancel,
            IcChart,
            IcCheck,
            IcChevronLeft,
            IcChevronRight,
            IcClose,
            IcCreate,
            IcDelete,
            IcDropped,
            IcErrorAnilib,
            IcExplore,
            IcFall,
            IcFilter,
            IcFire,
            IcForum,
            IcForumOutline,
            IcGroup,
            IcGroupOutline,
            IcHappy,
            IcHeart,
            IcHeartOutline,
            IcHide,
            IcHome,
            IcHomeOutline,
            IcHourglass,
            IcInfo,
            IcInfoOutline,
            IcLibraryBooks,
            IcLogin,
            IcLogout,
            IcMarkdownGif,
            IcMarkdownMediaPlay,
            IcMarkdownYoutube,
            IcMedia,
            IcMediaOutline,
            IcMessage,
            IcMinus,
            IcMoreHoriz,
            IcMoreVert,
            IcNeutral,
            IcNotification,
            IcNotificationOutline,
            IcPalette,
            IcPaused,
            IcPercent,
            IcPerson,
            IcPersonAdd,
            IcPersonCheck,
            IcPersonCheckOutline,
            IcPersonOutline,
            IcPin,
            IcPlanning,
            IcPlay,
            IcPlus,
            IcQuestionMark,
            IcRecommendation,
            IcReview,
            IcSad,
            IcSadAnilib,
            IcSave,
            IcSearch,
            IcSettings,
            IcSpring,
            IcStar,
            IcStarOutline,
            IcStats,
            IcStudio,
            IcSummer,
            IcThumbDown,
            IcThumbUp,
            IcTime,
            IcTune,
            IcTv,
            IcVoice,
            IcWatch,
            IcWatching,
            IcWinter,
            IcArrowDownward,
            IcArrowUpward,
            IcHistory,
            IcBold,
            IcItalics,
            IcStrikeThrough,
            IcSpoiler,
            IcLink,
            IcImage,
            IcYoutube,
            IcVideo,
            IcOrderedList,
            IcList,
            IcHeader,
            IcCenter,
            IcQuote,
            IcCode,
            IcPencil,
            IcGithub,
            IcLayoutStyle,
            IcMainInline,
            IcPublic,
            IcUnfold,
            IcPrivacyPolicy,
            IcBugReport,
            IcAssignment,
            IcWidgets,
            IcReply,
            IcTranslate
        )
        return __AllIcons!!
    }


@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun AppIconPreview() {
    FlowRow {
        AppIcons.AllIcons.forEach {
            Icon(modifier = Modifier.size(24.dp), imageVector = it, contentDescription = null)
        }
    }
}