// 默认情况下，Kotlin 变量不能持有 null 值
// val str :String  = null
// 02.nullsafe.kt:1:20: error: null cannot be a value of a non-null type 'String'.
// val str :String  = null


// kotlinc 02.nullsafe.kt  编译通过
val str2: String? = null

// Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable 
// receiver of type String?kotlin(UNSAFE_CALL)
// val str3 = str2.substring(0, 1)

val str3 = str2?.substring(0, 1) // 无异常

fun main(){
    println(str3) // null

    // ? 表示如果 str2 为 null，则 str3 也为 null，不会抛出异常

    // ?: 用于处理一个可能为空的变量，如果变量为 null，则使用 ?: 后面的默认值
    val str4 = str2?.substring(0, 1) ?: "default"
    println(str4) // "default"


    // !! 用于强制解包一个可能为空的变量，如果变量为 null，则会抛出异常
    val str5 = str2!!.substring(0, 1) // 如果 str2 为 null，会抛出异常
    println(str5)
    // Exception in thread "main" java.lang.NullPointerException
    //    at _02_nullsafeKt.main(02.nullsafe.kt:27)
    //    at _02_nullsafeKt.main(02.nullsafe.kt)
}