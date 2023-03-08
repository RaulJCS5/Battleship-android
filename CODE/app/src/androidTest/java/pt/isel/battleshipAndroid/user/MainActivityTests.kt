package pt.isel.battleshipAndroid.user

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.battleshipAndroid.main.*
import pt.isel.battleshipAndroid.utils.*

@RunWith(AndroidJUnit4::class)
class MainActivityTests {

    @Test
    fun uses_application_context() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("pt.isel.battleship_android", appContext.packageName)
    }

    @get:Rule
    val testRule = createAndroidComposeRule<MainActivity>()
    @Test
    fun screen_contains_all_navigation_options_except_back() {

        // Assert
        testRule.onNodeWithTag(NavigateMainRegisterButtonTestTag).assertExists()
        testRule.onNodeWithTag(NavigateMainLoginButtonTestTag).assertExists()
        testRule.onNodeWithTag(NavigateMainLeaderboardButtonTestTag).assertExists()
        testRule.onNodeWithTag(NavigateMainAuthorButtonTestTag).assertExists()
    }

}