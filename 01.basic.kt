/*
kotlionc编译器
https://github.com/JetBrains/kotlin/releases

export KOTLIN_HOME="/Users/guo/AIO/env/kotlinc/kotlinc_2_4_20"
export PATH="$KOTLIN_HOME/bin:$PATH"

编译
kotlionc 01.basic.kt
运行
java -cp .:$KOTLIN_HOME/lib/kotlin-stdlib.jar _01_basicKt
*/

// hello.kt
fun main() {
    println("Hello, World!")
}