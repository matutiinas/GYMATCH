package com.gymatch.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gymatch.shared.DiscoverViewModel
import com.gymatch.shared.FakeProfileRepository
import com.gymatch.shared.GetDiscoverProfilesUseCase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = DiscoverViewModel(
            getDiscoverProfiles = GetDiscoverProfilesUseCase(FakeProfileRepository())
        )

        setContent {
            DiscoverScreen(viewModel = viewModel)
        }
    }
}