package com.revolgenx.anilib.common.ui.component.date

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomSheet(
    openBottomSheet: MutableState<Boolean>,
    initialSelectedDateMillis: Long?,
    onDateSelected: (selectedDateMillis: Long) -> Unit
) {
    if (openBottomSheet.value) {
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = initialSelectedDateMillis)
        val confirmEnabled =
            remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            onDismissRequest = { openBottomSheet.value = false },
        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                BottomSheetConfirmation(
                    confirmEnabled = confirmEnabled.value,
                    dismissClicked = {
                        openBottomSheet.value = false
                    },
                    confirmClicked = {
                        openBottomSheet.value = false
                        onDateSelected(datePickerState.selectedDateMillis!!)
                    }
                )
                DatePicker(state = datePickerState)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarRangeBottomSheet(
    openBottomSheet: MutableState<Boolean>,
    initialSelectedStartDateMillis: Long?,
    initialSelectedEndDateMillis: Long?,
    rangeLessThan: Long? = 604800000,
    onDateRangeSelected: (selectedStartDateMillis: Long, selectedEndDateMillis: Long) -> Unit
) {
    if (openBottomSheet.value) {
        val datePickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = initialSelectedStartDateMillis,
            initialSelectedEndDateMillis = initialSelectedEndDateMillis
        )

        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedEndDateMillis != null && rangeLessThan?.let { (datePickerState.selectedEndDateMillis!! - datePickerState.selectedStartDateMillis!!) < rangeLessThan } ?: true }
        }

        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            onDismissRequest = { openBottomSheet.value = false },
        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                BottomSheetConfirmation(
                    confirmEnabled = confirmEnabled.value,
                    dismissClicked = {
                        openBottomSheet.value = false
                    },
                    confirmClicked = {
                        openBottomSheet.value = false
                        onDateRangeSelected(
                            datePickerState.selectedStartDateMillis!!,
                            datePickerState.selectedEndDateMillis!!
                        )
                    }
                )
                val dateFormatter: DatePickerFormatter =
                    remember { DatePickerDefaults.dateFormatter() }

                DateRangePicker(
                    state = datePickerState,
                    dateFormatter = dateFormatter,
                    headline = {
                        DateRangePickerDefaults.DateRangePickerHeadline(
                            selectedStartDateMillis = datePickerState.selectedStartDateMillis,
                            selectedEndDateMillis = datePickerState.selectedEndDateMillis,
                            displayMode = datePickerState.displayMode,
                            dateFormatter,
                            modifier = Modifier.padding(DatePickerHeadlinePadding)
                        )
                    })
            }
        }


    }
}


private val DatePickerHeadlinePadding =
    PaddingValues(start = 24.dp, end = 12.dp, bottom = 12.dp)
