package pt.isel.battleshipAndroid.main

import androidx.activity.ComponentActivity
import pt.isel.battleshipAndroid.DependenciesContainer

open class BaseActivity : ComponentActivity() { //Experiment
    protected val dependencies by lazy { application as DependenciesContainer }
}
