package com.omar.retromp3recorder.app.screens.main.components.menu.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.omar.retromp3recorder.app.R

@Composable
fun PopupComposable(
    title: String,
    buttonList: List<PopupButtonData>,
    content: @Composable () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = {
        onDismiss.invoke()
    }, content = {
        Surface(
            Modifier
                .fillMaxWidth()
                .background(
                    colorResource(id = R.color.popup_color), shape = RoundedCornerShape(corners)
                )
        ) {
            ConstraintLayout(
                Modifier
                    .heightIn(min = 170.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val (titleView, contentView, buttonsContainer) = createRefs()
                Text(text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(
                            bottom = 16.dp,
                            start = 8.dp
                        )
                        .constrainAs(titleView) {
                            width = Dimension.fillToConstraints
                            height = Dimension.wrapContent
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo((parent.top))
                        })
                Box(modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(contentView) {
                        width = Dimension.fillToConstraints
                        top.linkTo(titleView.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(buttonsContainer.top)
                    }) {
                    content.invoke()
                }
                Row(horizontalArrangement = Arrangement.End,
                    modifier = Modifier.constrainAs(buttonsContainer) {
                        width = Dimension.fillToConstraints
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                    buttonList.forEach {
                        PopupButton(
                            text = it.text,
                            isEnabled = it.isEnabled,
                            onClick = it.onClick,
                            color =
                            if (it.isEnabled) MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    PopupButton(
                        text = stringResource(id = R.string.popup_cancel),
                        isEnabled = true,
                        onClick = onDismiss,
                        color = colorResource(
                            R.color.white
                        ),
                    )

                }
            }

        }
    })
}

class PopupButtonData(
    val text: String,
    val isEnabled: Boolean = true,
    val onClick: () -> Unit
)

@Composable
fun PopupButton(
    text: String, isEnabled: Boolean, color: Color, onClick: () -> Unit
) {
    Box(
        Modifier
            .alpha(if (isEnabled) 1f else 0.5f)
            .clickable(isEnabled, onClick = onClick)
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
            style = TextStyle(
                color = color, fontSize = 16.sp, fontWeight = FontWeight.W600
            ),
        )
    }
}

private val corners = 4.dp
