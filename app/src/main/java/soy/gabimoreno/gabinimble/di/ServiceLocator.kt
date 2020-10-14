package soy.gabimoreno.gabinimble.di

import soy.gabimoreno.gabinimble.coredata.di.coreDataModule
import soy.gabimoreno.gabinimble.coredb.di.coreDbModule
import soy.gabimoreno.gabinimble.corenetwork.di.coreNetworkModule
import soy.gabimoreno.gabinimble.presentation.main.list.mainListModule
import soy.gabimoreno.gabinimble.presentation.main.mainModule

val serviceLocator = listOf(
    appModule,
//    coursesModule, coursesListModule, courseDetailModule,
    mainModule, mainListModule,

    coreDataModule,

    coreNetworkModule,
    coreDbModule,
)
