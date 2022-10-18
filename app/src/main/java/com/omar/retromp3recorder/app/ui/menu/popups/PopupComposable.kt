package com.omar.retromp3recorder.app.ui.menu.popups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.RetroTheme

@Preview
@Composable
fun PopupComposable(
    title: String = "Hello", buttonList: List<PopupButtonData> = listOf(
        PopupButtonData(text = "xello", isEnabled = true, onClick = {

        }),
        PopupButtonData(text = "xello", isEnabled = true, onClick = {

        }),
        PopupButtonData(text = "xello", isEnabled = false, onClick = {

        }),
    ), content: @Composable () -> Unit = { Text(text = "hello") }, onDismiss: () -> Unit = {}
) {
    RetroTheme {
        Dialog(onDismissRequest = {
            onDismiss.invoke()
        }, content = {
            Box(
                Modifier
                    .heightIn(min = 170.dp)
                    .fillMaxWidth()
                    .background(
                        colorResource(id = R.color.popup_color), shape = RoundedCornerShape(corners)
                    )
            ) {
                ConstraintLayout(
                    Modifier
                        .fillMaxWidth()

                        .padding(16.dp)
                ) {
                    val (titleView, contentView, buttonsContainer) = createRefs()
                    Text(text = title,
                        style = TextStyle(
                            color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.W600
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp, start = 8.dp)
                            .constrainAs(titleView) {
                                width = Dimension.fillToConstraints
                                height = Dimension.wrapContent
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo((parent.top))
                            })
                    Box(modifier = Modifier.constrainAs(contentView) {
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
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
                                color = colorResource(
                                    id = if (it.isEnabled) R.color.log_text_color
                                    else R.color.popup_button_deactivated
                                ),
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
}

class PopupButtonData(
    val text: String, val isEnabled: Boolean = true, val onClick: () -> Unit
)

@Composable
fun PopupButton(
    text: String, isEnabled: Boolean, color: Color, onClick: () -> Unit
) {
    Box(Modifier.clickable(isEnabled, onClick = onClick)) {
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
