cmake_minimum_required(VERSION 3.22.1)
project("miningcore")

add_library(
    miningcore
    SHARED
    miningcore.cpp
)

# Apply ARM64-specific optimization flags only for arm64-v8a
if(ANDROID_ABI STREQUAL "arm64-v8a")
    target_compile_options(miningcore PRIVATE -march=armv8-a+crypto)
endif()

find_library(log-lib log)

target_link_libraries(
    miningcore
    ${log-lib}
)
