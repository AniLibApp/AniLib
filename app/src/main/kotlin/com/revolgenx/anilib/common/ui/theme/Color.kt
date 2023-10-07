package com.revolgenx.anilib.common.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ext.SecondaryItemAlpha
import com.revolgenx.anilib.common.util.colorAtElevation


val md_theme_light_primary = Color(0xFF02A4F8)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFc5ebff)
val md_theme_light_onPrimaryContainer = Color(0xFF001D32)

val md_theme_light_surface = Color(0xFFf2fbff)
val md_theme_light_surface_container = Color(0xFFe5f6ff)
val md_theme_light_onSurface = Color(0xFF282B2E)
val md_theme_light_surfaceVariant = Color(0xFFd8f2ff)
val md_theme_light_onSurfaceVariant = md_theme_light_onSurface.copy(SecondaryItemAlpha)

val md_theme_light_secondary = Color(0xFF51606F)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = colorAtElevation(md_theme_light_surface_container, md_theme_light_primary, ElevationTokens.Level3)
val md_theme_light_onSecondaryContainer = colorAtElevation(md_theme_light_onSurfaceVariant, md_theme_light_primary, ElevationTokens.Level3)

val md_theme_light_tertiary = Color(0xFF7C5800)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFDEA8)

val md_theme_light_onTertiaryContainer = Color(0xFF271900)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)

val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFCFCFF)

val md_theme_light_onBackground = Color(0xFF1A1C1E)

val md_theme_light_outline = Color(0xFF72787E)
val md_theme_light_inverseOnSurface = Color(0xFFF0F0F4)
val md_theme_light_inverseSurface = Color(0xFF2F3033)
val md_theme_light_inversePrimary = Color(0xFF94CCFF)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF006399)
val md_theme_light_outlineVariant = Color(0xFFC2C7CF)
val md_theme_light_scrim = Color(0x4D000000)

val md_theme_dark_primary = Color(0xFF94CCFF)
val md_theme_dark_onPrimary = Color(0xFF003352)
val md_theme_dark_primaryContainer = Color(0xFF004B74)
val md_theme_dark_onPrimaryContainer = Color(0xFFCDE5FF)

val md_theme_dark_surface = Color(0xFF1A1C1E)
val md_theme_dark_surface_container = Color(0xFF1A1C1E)
val md_theme_dark_onSurface = Color(0xFFE2E2E5)
val md_theme_dark_surfaceVariant = Color(0xFF42474E)
val md_theme_dark_onSurfaceVariant = Color(0xFFC2C7CF)

val md_theme_dark_secondary = Color(0xFFB9C8DA)
val md_theme_dark_onSecondary = Color(0xFF233240)
val md_theme_dark_secondaryContainer = colorAtElevation(md_theme_dark_surface_container, md_theme_dark_primary, ElevationTokens.Level3)
val md_theme_dark_onSecondaryContainer = colorAtElevation(md_theme_dark_onSurface, md_theme_dark_primary, ElevationTokens.Level3)

val md_theme_dark_tertiary = Color(0xFFFABC41)
val md_theme_dark_onTertiary = Color(0xFF422D00)
val md_theme_dark_tertiaryContainer = Color(0xFF5E4200)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFDEA8)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF1A1C1E)
val md_theme_dark_onBackground = Color(0xFFE2E2E5)
val md_theme_dark_outline = Color(0xFF8C9198)
val md_theme_dark_inverseOnSurface = Color(0xFF1A1C1E)
val md_theme_dark_inverseSurface = Color(0xFFE2E2E5)
val md_theme_dark_inversePrimary = Color(0xFF006399)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFF94CCFF)
val md_theme_dark_outlineVariant = Color(0xFF42474E)
val md_theme_dark_scrim = Color(0x4D000000)

val review_list_gradient_top = Color(0x0)
val review_list_gradient_bottom = Color(0xB3000000)

val seed = Color(0xFF02A4F8)


val status_current = Color(0xFF42A5F5)
val status_planning = Color(0xFFF09967)
val status_completed = Color(0xFF7AD358)
val status_dropped = Color(0xFFF8375B)
val status_paused = Color(0xFFF37A7D)
val status_repeating = Color(0xFF9C27B0)


val status_finished = Color(0xFF42A5F5)
val status_releasing = Color(0xFF00C853)
val status_not_yet_released = Color(0xFF673AB7)
val status_cancelled = Color(0xFFD50000)
val status_hiatus = Color(0xFFFF6E40)
val status_unknown = Color(0xFFD50000)

val logout_color = Color(0xFFF8375B)
val support_color = Color(0xFFE85D75)

val rank_type_popular = Color(0xFFE85D75)
val rank_type_rated = Color(0xFFF7BF63)



internal object ElevationTokens {
    val Level0 = 0.0.dp
    val Level1 = 1.0.dp
    val Level2 = 3.0.dp
    val Level3 = 6.0.dp
    val Level4 = 8.0.dp
    val Level5 = 12.0.dp
}