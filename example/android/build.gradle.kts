// Copyright 2025, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.composeCompiler)
    id("module.kotlin-jvm-toolchain")
    id("module.spotless")
}

dependencies {
    implementation(projects.example.shared)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))
}

@Suppress("UnstableApiUsage")
android {
    buildToolsVersion = BuildConfig.BUILD_TOOLS_VERSION
    compileSdk {
        version =
            release(BuildConfig.COMPILE_SDK) {
                minorApiLevel = BuildConfig.COMPILE_SDK_MINOR
            }
    }
    defaultConfig {
        applicationId = BuildConfig.APPLICATION_ID
        minSdk = BuildConfig.MIN_SDK
        targetSdk = BuildConfig.TARGET_SDK
        versionName = BuildConfig.APPLICATION_VERSION_NAME
        versionCode = getGitVersionCode()
    }
    experimentalProperties["android.experimental.r8.dex-startup-optimization"] = true
    namespace = BuildConfig.APPLICATION_ID
   
    buildTypes {
        release {
            optimization.enable = true
            vcsInfo.include = false
        }
        debug {
        }
        create("nonMinifiedRelease") {
            signingConfig = signingConfigs.getByName("debug")
        }
        create("benchmarkRelease") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += "release"
        }
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    packaging {
        jniLibs {
            excludes += "lib/*/libandroidx.graphics.path.so"
        }
    }
}

androidComponents {
    onVariants(selector().withBuildType("release")) {
        it.packaging.resources.excludes
            .add("**")
    }
}

base {
    archivesName.set(
        "${BuildConfig.APPLICATION_NAME}-v${BuildConfig.APPLICATION_VERSION_NAME}(${getGitVersionCode()})",
    )
}
