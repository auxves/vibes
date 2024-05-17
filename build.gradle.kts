import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

plugins {
	id("fabric-loom")
	kotlin("jvm")
}

dependencies {
	minecraft("com.mojang:minecraft:${properties["minecraft"]}")
	mappings("net.fabricmc:yarn:${properties["yarn"]}:v2")

	modImplementation("net.fabricmc:fabric-loader:${properties["loader"]}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fabric_api"]}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${properties["fabric_kotlin"]}")
}

tasks.named<Copy>("processResources") {
	duplicatesStrategy = DuplicatesStrategy.INCLUDE

	from(sourceSets["main"].resources.srcDirs) {
		include("fabric.mod.json")
		expand(properties)
	}

	from(sourceSets["main"].resources.srcDirs) {
		exclude("fabric.mod.json")
	}
}

tasks.withType<Jar> {
	from("LICENSE")
}
