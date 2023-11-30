./gradlew flywayClean flywayMigrate -Dflyway.configFiles=./flyway/flyway-local-migration.properties -i
./gradlew clean build
./gradlew flywayMigrate -Dflyway.configFiles=./flyway/flyway-local-provisioning.properties -i
