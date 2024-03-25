package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.material.icons.materialIcon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import com.revolgenx.anilib.common.ui.icons.AppIcons


val AppIcons.IcOrderedList: ImageVector
    get() {
        if (_icOrderedList != null) {
            return _icOrderedList!!
        }
        _icOrderedList = materialIcon(name = "IcOrderedList") {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 1.0F,
                strokeAlpha = 1.0F,
                strokeLineWidth = 0.0F,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 4.0F,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(2.0F, 17.0F)
                horizontalLineToRelative(2.0F)
                verticalLineToRelative(0.5F)
                lineTo(3.0F, 17.5F)
                verticalLineToRelative(1.0F)
                horizontalLineToRelative(1.0F)
                verticalLineToRelative(0.5F)
                lineTo(2.0F, 19.0F)
                verticalLineToRelative(1.0F)
                horizontalLineToRelative(3.0F)
                verticalLineToRelative(-4.0F)
                lineTo(2.0F, 16.0F)
                verticalLineToRelative(1.0F)

                moveTo(3.0F, 8.0F)
                horizontalLineToRelative(1.0F)
                lineTo(4.0F, 4.0F)
                lineTo(2.0F, 4.0F)
                verticalLineToRelative(1.0F)
                horizontalLineToRelative(1.0F)
                verticalLineToRelative(3.0F)

                moveTo(2.0F, 11.0F)
                horizontalLineToRelative(1.8F)
                lineTo(2.0F, 13.1F)
                verticalLineToRelative(0.9F)
                horizontalLineToRelative(3.0F)
                verticalLineToRelative(-1.0F)
                lineTo(3.2F, 13.0F)
                lineTo(5.0F, 10.9F)
                lineTo(5.0F, 10.0F)
                lineTo(2.0F, 10.0F)
                verticalLineToRelative(1.0F)

                moveTo(7.0F, 5.0F)
                verticalLineToRelative(2.0F)
                horizontalLineToRelative(14.0F)
                lineTo(21.0F, 5.0F)
                lineTo(7.0F, 5.0F)

                moveTo(7.0F, 19.0F)
                horizontalLineToRelative(14.0F)
                verticalLineToRelative(-2.0F)
                lineTo(7.0F, 17.0F)
                verticalLineToRelative(2.0F)

                moveTo(7.0F, 13.0F)
                horizontalLineToRelative(14.0F)
                verticalLineToRelative(-2.0F)
                lineTo(7.0F, 11.0F)
                verticalLineToRelative(2.0F)
                close()
            }
        }
        return _icOrderedList!!
    }

private var _icOrderedList: ImageVector? = null

