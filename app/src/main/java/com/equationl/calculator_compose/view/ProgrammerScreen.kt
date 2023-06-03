package com.equationl.calculator_compose.view

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.equationl.calculator_compose.dataModel.InputBase
import com.equationl.calculator_compose.dataModel.programmerFunctionKeyBoardBtn
import com.equationl.calculator_compose.dataModel.programmerNumberKeyBoardBtn
import com.equationl.calculator_compose.ui.theme.*
import com.equationl.calculator_compose.utils.formatNumber
import com.equationl.calculator_compose.view.widgets.AutoSizeText
import com.equationl.calculator_compose.viewModel.ProgrammerAction
import com.equationl.calculator_compose.viewModel.ProgrammerViewModel

@Composable
fun ProgrammerScreen(
    viewModel: ProgrammerViewModel = hiltViewModel()
) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween) {
        // 左侧键盘
        Row(modifier = Modifier.weight(1.3f)) {
            FunctionKeyBoard(viewModel = viewModel)
        }

        Divider(modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .padding(vertical = 16.dp, horizontal = 0.dp))

        // 显示数据
        Row(modifier = Modifier.weight(2f)) {
            CenterScreen(viewModel = viewModel)
        }

        Divider(modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .padding(vertical = 16.dp, horizontal = 0.dp))

        // 右侧键盘
        Row(modifier = Modifier.weight(1.5f)) {
            NumberBoard(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CenterScreen(viewModel: ProgrammerViewModel) {
    val viewState = viewModel.viewStates
    Column(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            // 计算公式
            AnimatedContent(targetState = viewState.showText) { targetState: String ->
                SelectionContainer {
                    Text(
                        text = targetState,
                        modifier = Modifier.padding(8.dp),
                        fontSize = ShowNormalFontSize,
                        fontWeight = FontWeight.Light,
                        color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                    )
                }
            }
            // 输入值或计算结果
            AnimatedContent(
                targetState = viewState.inputValue,
                transitionSpec = {
                    if (targetState.length > initialState.length) {
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        slideInVertically { height -> -height } + fadeIn() with
                                slideOutVertically { height -> height } + fadeOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                }
            ) { targetState: String ->
                Row(modifier = Modifier.padding(8.dp)) {
                    SelectionContainer {
                        AutoSizeText(
                            text = targetState.formatNumber(
                                formatDecimal = false, // 程序员计算没有小数
                                addSplitChar = if (viewState.inputBase == InputBase.DEC) "," else " ",
                                splitLength = if (viewState.inputBase == InputBase.HEX || viewState.inputBase == InputBase.BIN) 4 else 3,
                                isAddLeadingZero = false, // 即使是二进制，在输入时也不应该有前导0
                                formatInteger = true
                            )
                            ,
                            fontSize = InputLargeFontSize,
                            fontWeight = FontWeight.Bold,
                            color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { viewModel.dispatch(ProgrammerAction.ChangeInputBase(InputBase.HEX)) }
            ) {
                Text(
                    text = "HEX",
                    fontSize =
                    if (viewState.inputBase == InputBase.HEX) InputTitleContentSize
                    else InputNormalFontSize,
                    fontWeight = if (viewState.inputBase == InputBase.HEX) FontWeight.Bold else null,
                    color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                )

                SelectionContainer {
                    Text(
                        text = viewState.inputHexText.formatNumber(addSplitChar = " ", splitLength = 4),
                        fontSize = InputNormalFontSize,
                        modifier = Modifier.padding(start = 8.dp),
                        color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { viewModel.dispatch(ProgrammerAction.ChangeInputBase(InputBase.DEC)) }
            ) {
                Text(
                    text = "DEC",
                    fontSize =
                    if (viewState.inputBase == InputBase.DEC) InputTitleContentSize
                    else InputNormalFontSize,
                    fontWeight = if (viewState.inputBase == InputBase.DEC) FontWeight.Bold else null,
                    color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                )

                SelectionContainer {
                    Text(
                        text = viewState.inputDecText.formatNumber(),
                        fontSize = InputNormalFontSize,
                        modifier = Modifier.padding(start = 8.dp),
                        color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                    )
                }

            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { viewModel.dispatch(ProgrammerAction.ChangeInputBase(InputBase.OCT)) }
            ) {
                Text(
                    text = "OCT",
                    fontSize =
                    if (viewState.inputBase == InputBase.OCT) InputTitleContentSize
                    else InputNormalFontSize,
                    fontWeight = if (viewState.inputBase == InputBase.OCT) FontWeight.Bold else null,
                    color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                )

                SelectionContainer {
                    Text(
                        text = viewState.inputOctText.formatNumber(addSplitChar = " "),
                        fontSize = InputNormalFontSize,
                        modifier = Modifier.padding(start = 8.dp),
                        color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                    )
                }

            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { viewModel.dispatch(ProgrammerAction.ChangeInputBase(InputBase.BIN)) }
            ) {
                Text(
                    text = "BIN",
                    fontSize =
                    if (viewState.inputBase == InputBase.BIN) InputTitleContentSize
                    else InputNormalFontSize,
                    fontWeight = if (viewState.inputBase == InputBase.BIN) FontWeight.Bold else null,
                    color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                )

                SelectionContainer {
                    Text(
                        text = viewState.inputBinText.formatNumber(addSplitChar = " ", splitLength = 4, isAddLeadingZero = viewState.inputBinText != "0"),
                        fontSize = InputNormalFontSize,
                        modifier = Modifier
                            .padding(start = 8.dp),
                        color = if (MaterialTheme.colors.isLight) Color.Unspecified else MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun NumberBoard(viewModel: ProgrammerViewModel) {
    val viewState = viewModel.viewStates

    Column(modifier = Modifier.fillMaxSize()) {
        for (btnRow in programmerNumberKeyBoardBtn()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                for (btn in btnRow) {
                    val isAvailable = if (btn.isAvailable) {
                        btn.index !in viewState.inputBase.forbidBtn
                    }
                    else {
                        false
                    }

                    Row(modifier = Modifier.weight(1f)) {
                        KeyBoardButton(
                            text = btn.text,
                            onClick = { viewModel.dispatch(ProgrammerAction.ClickBtn(btn.index)) },
                            isAvailable = isAvailable,
                            backGround = btn.background,
                            isFilled = btn.isFilled,
                            paddingValues = PaddingValues(0.5.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FunctionKeyBoard(viewModel: ProgrammerViewModel) {
    val viewState = viewModel.viewStates

    Column(modifier = Modifier.fillMaxSize()) {
        for (btnRow in programmerFunctionKeyBoardBtn()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                for (btn in btnRow) {
                    val isAvailable = if (btn.isAvailable) {
                        btn.index !in viewState.inputBase.forbidBtn
                    }
                    else {
                        false
                    }

                    Row(modifier = Modifier.weight(1f)) {
                        KeyBoardButton(
                            text = btn.text,
                            onClick = { viewModel.dispatch(ProgrammerAction.ClickBtn(btn.index)) },
                            isAvailable = isAvailable,
                            backGround = btn.background,
                            isFilled = btn.isFilled,
                            paddingValues = PaddingValues(0.5.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun KeyBoardButton(
    text: String,
    onClick: () -> Unit,
    isAvailable: Boolean = true,
    backGround: Color = Color.White,
    isFilled: Boolean = false,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Card(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        backgroundColor = if (isFilled) backGround else MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.large,
        elevation = 0.dp,
        border = BorderStroke(0.dp, Color.Transparent),
        enabled = isAvailable
    ) {
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text,
                fontSize = 24.sp,
                color = if (isAvailable) {
                    if (isFilled) Color.Unspecified else backGround
                } else {
                    if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
                }
            )
        }
    }
}

@Preview(showSystemUi = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 720)
@Composable
fun PreviewProgrammerScreen() {
    CalculatorComposeTheme(false) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {
            ProgrammerScreen(ProgrammerViewModel())
        }
    }
}

@Preview(showSystemUi = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 720)
@Composable
fun PreviewProgrammerScreenDark() {
    CalculatorComposeTheme(true) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {
            ProgrammerScreen(ProgrammerViewModel())
        }
    }
}