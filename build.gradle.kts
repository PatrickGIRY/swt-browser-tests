plugins {
    id("java")
    id("application")
}

group = "poc"
version = "1.0-SNAPSHOT"

application {
    applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
    mainClass = "poc.swt.browser.tests.app.Main"
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "poc.swt.browser.tests.app.Main")
    }
}

repositories {
    mavenCentral()
}

configurations.all {
    resolutionStrategy {
        dependencySubstitution {
            val os = System.getProperty("os.name").lowercase()
            val arch = System.getProperty("os.arch").lowercase()
            when {
                os.contains("windows") -> {
                    substitute(module("org.eclipse.platform:org.eclipse.swt.\${osgi.platform}"))
                        .using(module("org.eclipse.platform:org.eclipse.swt.win32.win32.x86_64:3.130.0"))
                }
                os.contains("linux") -> {
                    substitute(module("org.eclipse.platform:org.eclipse.swt.\${osgi.platform}"))
                        .using(module("org.eclipse.platform:org.eclipse.swt.gtk.linux.x86_64:3.130.0"))
                }
                os.contains("mac") -> {
                    if (arch.contains("aarch64") || arch.contains("arm64")) {
                        substitute(module("org.eclipse.platform:org.eclipse.swt.\${osgi.platform}"))
                            .using(module("org.eclipse.platform:org.eclipse.swt.cocoa.macosx.aarch64:3.130.0"))
                    } else {
                        substitute(module("org.eclipse.platform:org.eclipse.swt.\${osgi.platform}"))
                            .using(module("org.eclipse.platform:org.eclipse.swt.cocoa.macosx.x86_64:3.130.0"))
                    }
                }
            }
        }
    }
}

dependencies {
    implementation("commons-io:commons-io:2.14.0")
    implementation("org.eclipse.platform:org.eclipse.ui:3.207.200")
    implementation("org.eclipse.platform:org.eclipse.swt.\${osgi.platform}:3.130.0")
    implementation("org.slf4j:slf4j-api:2.0.17")
    val log4jVersion = "2.25.3"
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}