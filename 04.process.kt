// 流程
fun main() {

    // if 同Java
    val x = 5
    if (x > 0) {
        println("x is positive") // x is positive
    } else if (x < 0) {
        println("x is negative")
    } else {
        println("x is zero")
    }

    // if 接受返回值 Kt特性
    val y = if (x > 0){
         "positive"
    } else if (x < 0){
        "negative"
    } else{
        "zero"
    }
    println(y) // "positive"



    // kt没有switch语法,switch也不是保留关键字, 可以用作普通变量名
    val switch = 10
    println(switch) // 10

    // Java的switch
    /*
       传统switch：byte/short/char/int + 包装类、String、enum
       Java 21+：任意引用类型

       原始类型始终不支持 long/float/double/boolean

        // Java 14（正式标准）：switch 表达式在 JEP 361 中被正式确定为标准语言特性
        int b = 10;
        String str = switch(b){
            case 10 -> "12";
            default -> "121"; // 必须有一个默认值
        };

       // Java21+
       // 支持when守卫
        static void test(Object obj) {
            switch (obj) {
                // 匹配 String 类型,并且长度等于 1
                case String s when s.length() == 1 -> 
                    System.out.println("Short: " + s);
                // 匹配 String 类型,但不满足上面的条件
                case String s -> 
                    System.out.println(s);
                // 其他类型
                default -> 
                    System.out.println("Not a string");
            }
        }

        // 支持任意引用类型
        record Point(int x, int y) { }
        enum Color { RED, GREEN, BLUE }

        static void typeTester(Object obj) {
            switch (obj) {
                case null -> 
                    System.out.println("null");
                case String s -> 
                    System.out.println("String");
                case Color c -> 
                    System.out.println("Color with " + c.values().length + " values");
                case Point p -> 
                    System.out.println("Record class: " + p.toString());
                case int[] ia -> 
                    System.out.println("Array of int values of length " + ia.length);
                default -> 
                    System.out.println("Something else");
            }
        }
       
     */



    // kt中对应更为强大的when
    /*
        when 支持任意类型的条件判断,包括范围、多个值、自定义条件等
     */

    // 无参数
    when {
        x > 0 -> println("正数")
        x < 0 -> println("负数")
        else -> println("零")
    }

    val tmp = 12;
    val s = when (val x = tmp* 10) {       // 捕获主语 + 作为表达式, 对x可以是一个表达式处理后的结果,when的内部使用这个结果作为x的值
        0, 120 -> "0 或 120"               // 多常量匹配
        is Int -> "其他整数：$x"           // 类型判断 + 智能转换 x 自动转为 Int
        null -> "null"                     // null 匹配
        else -> "其他类型"                 // 默认分支,无需 break
    }
    println(s) // 输出"0 或 120"










    // kt的for循环
    // 范围遍历
    for (i in 1..5) println(i)          // 1 2 3 4 5
    for (i in 1 until 5) println(i)     // 1 2 3 4
    for (i in 5 downTo 1) println(i)    // 5 4 3 2 1
    for (i in 1..10 step 2) println(i)  // 1 3 5 7 9

    // 数组/集合
    val arr = arrayOf("a", "b", "c")
    for (s in arr) println(s)
    // 支持索引遍历：indices、withIndex()
    for (i in arr.indices) println(arr[i])
    for ((index, value) in arr.withIndex()) {
        println("$index -> $value")
    }
    // 0 -> a
    // 1 -> b
    // 2 -> c
    // Map 解构
    val map = mapOf(1 to "one", 2 to "two")
    for ((key, value) in map) {
        println("$key = $value") 
    }
    // 1 = one
    // 2 = two

    // while 在两种语言中几乎完全一致,
    // 主要区别是 Kotlin 的类型系统更严格,但语法和语义相同

    // do-while 语义完全相同
    
}

