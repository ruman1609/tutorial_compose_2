/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.affirmations

import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.affirmations.data.Datasource
import com.example.affirmations.ui.theme.AffirmationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AffirmationApp()
        }
    }
}

@Composable
private fun AffirmationApp() {
    val state = rememberLazyListState()
    val dataSource = Datasource.loadAffirmations().toMutableStateList()

    AffirmationsTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
            LazyColumn(
                state = state,
                modifier = Modifier.padding(4.dp),
                contentPadding = PaddingValues(0.dp, 8.dp)
            ) {
                itemsIndexed(dataSource) { ind, affirmation ->
                    AffirmationItemList(
                        painter = painterResource(id = affirmation.imageRes),
                        title = stringResource(affirmation.titleRes)
                    ) { dataSource.removeAt(ind) }
                }
            }
        }
    }
}

private var toast: Toast? = null

@Composable
private fun AffirmationItemList(painter: Painter, title: String, deleteCallback: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 4.dp)
            .clickable(onClick = {
                toast?.cancel()
                toast = Toast.makeText(context, "Deleted: $title", Toast.LENGTH_SHORT)
                toast?.show()
                deleteCallback()
            }),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
    ) {
        Column {
            Image(
                painter = painter, contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AffirmationAppPreview() {
    AffirmationApp()
}
