package com.itformula

import kotlin.math.min



fun main(){
    // 普通调用函数
    val fn1:Int = fn1("Hello", 12)

    anonymousFun()

    callbackFun("add", {
        a,b -> a + b
    })
    callbackFun("min", {
            a,b -> min(a,b)
    })

    callbackFun("outer fun add", ::add)
    /*
     * Kotlin 里把函数当值传递，要用 ::
     * :: 就是"把某个东西（函数、属性、构造器）变成一个可传递的值"的操作符
     * 除此之外它还会和 @ 标签配合,出现在 return@label、continue@label 里
     * 核心 :: 用来取"引用", @ 用来打"标签"
     */
}

fun fn1(arg1:String = "", arg2: Int) :Int {
    println(arg1 + arg2)
    return arg2;
}


fun anonymousFun(){
    // -> 在 Lambda 里的作用是分隔"参数列表"和"函数体"

    // 1.匿名函数(定义参数类型和返回值类型 , 参数名在{}中定义)
    val func : (String, Int) -> Int = {
            str, i ->
        println(str + i)
        i
    }
    val funcNoReturn: (String, Int) -> Int = {
            str, i ->
        println(str + i)
        i
    }

    // 2.匿名函数(参数类型和参数名在{}中定义， 返回值自动推断)
    val func2 = {
            str:String, i:Int ->
        println(str + i)
        i
    }

    // 3.最"正统"的匿名函数
    val func3 = fun(str: String, i: Int): Int {
        println(str + i)
        return i
    }



    val a = func("Hello", 12)
    val b = funcNoReturn("Hello", 12)
    val c = func2("Hello", 12) // 12 ?
    val d = func3("Hello", 12)
    println("$a, $b, $c, $d")
}


fun callbackFun(flag: String, add: (Int, Int) -> Int) {
    val i = add(12, 12)
        // 参数名 add 在内层作用域，优先级更高
        // 内层同名会遮蔽外层，所以 add(12, 12) 永远指向参数
    println("$flag i = $i")
}

fun add(a: Int, b: Int): Int {//顶层函数 add 在外层作用域
    println("a = $a, b = $b")
    return a + b
}



/*
    Hello12
    Hello12
    Hello12
    Hello12
    Hello12
    12, 12, 12, 12
    add i = 24
    min i = 12
    a = 12, b = 12
    outer fun add i = 24
* */

/*
## 1. `::foo` 顶层/局部函数引用

**场景**：把顶层函数当参数传给 `map`。

kotlin

```
fun square(x: Int) = x * x

fun main() {
    val nums = listOf(1, 2, 3, 4)

    // 不用 lambda
    val r1 = nums.map { square(it) }
    // 用函数引用，更简洁
    val r2 = nums.map(::square)

    println(r1) // [1, 4, 9, 16]
    println(r2) // [1, 4, 9, 16]
}
```



**局部函数也能引用**：

kotlin

```
fun main() {
    fun isEven(n: Int) = n % 2 == 0

    val evens = listOf(1, 2, 3, 4, 5).filter(::isEven)
    println(evens) // [2, 4]
}
```



------

## 2. `obj::foo` 绑定成员函数引用

**场景**：已有对象实例，引用它的某个方法。

kotlin

```
class Printer(val prefix: String) {
    fun print(msg: String) = println("$prefix$msg")
}

fun main() {
    val p = Printer("[LOG] ")

    // 绑定到实例 p，类型是 (String) -> Unit
    val log = p::print

    log("启动")   // [LOG] 启动
    log("关闭")   // [LOG] 关闭

    // 当参数传
    listOf("a", "b").forEach(p::print)
    // [LOG] a
    // [LOG] b
}
```



**绑定后不需要再传实例**，因为实例已经"绑"进去了。

------

## 3. `Class::foo` 未绑定成员函数引用

**场景**：引用方法但还不知道作用于哪个实例，调用时再传实例。

kotlin

```
class User(val name: String) {
    fun greet() = "Hello, $name"
}

fun main() {
    // 未绑定：类型是 (User) -> String
    val greetRef = User::greet

    val u1 = User("Tom")
    val u2 = User("Jerry")

    println(greetRef(u1))  // Hello, Tom
    println(greetRef(u2))  // Hello, Jerry

    // 配合 map，把每个 User 转成字符串
    val users = listOf(User("A"), User("B"))
    println(users.map(User::greet))  // [Hello, A, Hello, B]
}
```



**和绑定版区别**：

kotlin

```
p::print       // 类型 (String) -> Unit      已绑定实例
Printer::print // 类型 (Printer, String) -> Unit  要传实例
```



------

## 4. `obj::prop` 属性引用

**场景**：读取/修改某个对象的属性，尤其配合反射或排序。

kotlin

```
class Config {
    var debug: Boolean = false
    val version: String = "1.0"
}

fun main() {
    val c = Config()

    val debugRef = c::debug    // 可读可写，KProperty1
    val verRef = c::version    // 只读

    println(debugRef.get())    // false
    debugRef.set(true)         // 修改
    println(c.debug)           // true

    println(verRef.get())      // 1.0
}
```



**实际用途**：排序/比较

kotlin

```
data class Person(val name: String, val age: Int)

fun main() {
    val people = listOf(Person("Tom", 25), Person("Ann", 20))

    // 按属性排序
    val sorted = people.sortedBy(Person::age)
    println(sorted) // [Person(name=Ann, age=20), Person(name=Tom, age=25)]
}
```



------

## 5. `Class::prop` 未绑定属性引用

**场景**：还不知道对象是谁，先拿到"取属性"的能力。

kotlin

```
data class Person(val name: String, val age: Int)

fun main() {
    // 未绑定，类型 (Person) -> Int
    val ageRef = Person::age

    val p = Person("Tom", 25)
    println(ageRef(p))  // 25

    // 直接当转换函数用
    val people = listOf(Person("A", 1), Person("B", 2))
    println(people.map(Person::name))  // [A, B]
}
```



**`map(Person::name)`** 是极常见的写法，等于 `map { it.name }`。

------

## 6. `::ClassName` 构造器引用

**场景**：工厂、批量创建对象。

kotlin

```
class User(val id: Int)

fun main() {
    // 构造器引用，类型 (Int) -> User
    val create = ::User

    val u = create(1)
    println(u.id)  // 1

    // 常见：把数字列表转成对象列表
    val users = listOf(1, 2, 3).map(::User)
    println(users.map { it.id })  // [1, 2, 3]
}
```



**Java 类也能引用构造器**：

kotlin

```
val list = listOf(1, 2, 3).map(::java.lang.Integer)  // 注意返回类型
```



**类引用 + 构造器组合**：

kotlin

```
class Person(val name: String)

fun main() {
    val personClass = Person::class               // KClass
    val ctor = personClass.primaryConstructor      // 反射拿构造器
    val p = ctor!!.call("Tom")
    println(p.name)  // Tom
}
```



------

## 7. `Class::class` KClass 类引用

**场景**：反射、判断类型、当 map 的 key。

kotlin

```
fun main() {
    val kc = String::class      // KClass<String>
    println(kc.simpleName)      // String
    println(kc.qualifiedName)   // kotlin.String

    // Java Class 互转
    val jc: Class<String> = String::class.java
    println(jc.name)            // java.lang.String

    // 反射：遍历属性
    data class User(val name: String, val age: Int)
    User::class.memberProperties.forEach {
        println(it.name)        // age, name（顺序不定）
    }
}
```



**常见用途：当 Map 的 key 分发**

kotlin

```
val registry = mutableMapOf<KClass<*>, () -> Any>()
registry[String::class] = { "hello" }
registry[Int::class] = { 42 }

fun get(kc: KClass<*>) = registry[kc]?.invoke()
println(get(String::class))  // hello
```



------

## 8. `this@Outer` 标签引用外部类

**场景**：内部类里要访问外部类成员，或 lambda 里访问外层 `this`。

kotlin

```
class Outer {
    val name = "Outer"

    inner class Inner {
        val name = "Inner"

        fun show() {
            println(this.name)         // Inner
            println(this@Outer.name)   // Outer
        }
    }
}

fun main() {
    Outer().Inner().show()
    // Inner
    // Outer
}
```



**lambda 里的 `this` 也常用标签**：

kotlin

```
class View {
    val id = "view1"

    fun setup() {
        // 假设 onClick 的 lambda 里有自己的 this
        button.setOnClickListener {
            println(this@View.id)  // 明确指向外层 View
        }
    }
}

class Button {
    fun setOnClickListener(action: Button.() -> Unit) {
        this.action()
    }
    var action: () -> Unit = {}
    val id = "button1"
}
```



**更实用的：`apply` / `with` 里区分 this**

kotlin

```
class Config {
    var name = "outer"

    fun build() {
        val result = StringBuilder().apply {
            append("name=")
            append(this@Config.name)  // 区分：这里指 Config 的 name
        }
        println(result)  // name=outer
    }
}
```



------

## 汇总：类型速查

| 写法             | 类型                          | 调用方式                      |
| :--------------- | :---------------------------- | :---------------------------- |
| `::square`       | `(Int) -> Int`                | `square(3)`                   |
| `p::print`       | `(String) -> Unit`            | `print("hi")`                 |
| `Printer::print` | `(Printer, String) -> Unit`   | `print(p, "hi")`              |
| `c::debug`       | `KProperty1<Config, Boolean>` | `ref.get()` / `ref.set(true)` |
| `Person::age`    | `(Person) -> Int`             | `age(p)`                      |
| `::User`         | `(Int) -> User`               | `User(1)`                     |
| `String::class`  | `KClass<String>`              | `kc.simpleName`               |
| `this@Outer`     | —                             | 直接访问成员                  |

------

## 最实用的三条记忆

1. **`::` 前面没东西** → 引用顶层/构造器：`::square`、`::User`
2. **`::` 前面是实例** → 绑定，不用再传实例：`p::print`
3. **`::` 前面是类名** → 未绑定，第一个参数要传实例：`Person::age`，常写成 `list.map(Person::name)`

实际开发里最常出现的就是 `list.map(::转换函数)`、`list.map(Class::属性)`、`this@Outer` 这几个。
*/