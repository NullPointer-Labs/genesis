plugins {
    id("application")
}

application {
    mainClass.set("application.Main")
}

dependencies {
    implementation(project(":Framework"))
    implementation("org.slf4j:slf4j-simple:2.0.12")
}