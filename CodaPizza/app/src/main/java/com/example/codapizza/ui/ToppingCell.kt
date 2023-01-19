package com.example.codapizza.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.codapizza.model.Topping
import com.example.codapizza.model.ToppingPlacement


@Preview
@Composable
private fun ToppingCellPreviewPlacementNull() {
    ToppingCell(
        topping = Topping.Pepperoni,
        placement = null,
        onClickTopping = {}
    )
}

@Preview
@Composable
private fun ToppingCellPreviewLeftHalf() {
    ToppingCell(
        topping = Topping.Pepperoni,
        placement = ToppingPlacement.Left,
        onClickTopping = {}
    )
}

@Composable
fun ToppingCell(
    topping: Topping,
    placement: ToppingPlacement?,
    onClickTopping: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = (placement != null),
            onCheckedChange = {

            })
        Column {
            Text(
                text = stringResource(topping.toppingName),
                style = MaterialTheme.typography.body1
            )

            if (placement != null) {
                Text(
                    text = stringResource(placement.label),
                    style = MaterialTheme.typography.body2
                )
            }

        }
    }
}

