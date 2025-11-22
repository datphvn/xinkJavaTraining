rootProject.name = "advanced-gradle-project"

include("core", "api", "web", "integration-tests")

project(":core").projectDir = file("modules/core")
project(":api").projectDir = file("modules/api") 
project(":web").projectDir = file("modules/web")
project(":integration-tests").projectDir = file("modules/integration-tests")

