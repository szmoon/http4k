description = "Functions to be used for testing of Apache OpenWhisk"

apply(plugin = "com.github.johnrengelman.shadow")

dependencies {
    api(project(":http4k-core"))
    api(project(":http4k-cloudnative"))
    api(project(":http4k-serverless-openwhisk"))
    api(project(":http4k-format-jackson"))
    api(project(":http4k-client-okhttp"))
    api(project(path = ":http4k-core", configuration ="testArtifacts"))
    api(project(path = ":http4k-serverless-core", configuration ="testArtifacts"))
    testImplementation("dev.forkhandles:bunting4k:_")
    testImplementation(project(path = ":http4k-core", configuration ="testArtifacts"))
}
